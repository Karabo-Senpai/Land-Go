package com.example.opsc7312_task2;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.search.CategorySearchEngine;
import com.mapbox.search.CategorySearchOptions;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.location.DefaultLocationProvider;
import com.mapbox.search.result.SearchResult;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMap extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener,MapboxMap.OnMapClickListener,FavouritesDialog.FavouriteDialogListener {


    /*
      GLOBAL VARIABLE DECLARATIONS
    */

    String directions_criteria;
    boolean traffic_settings;
    String favourite_landmark, favourite_landmark_desc;
    String SOURCE_ID = "SOURCE_ID";
    String ICON_ID = "ICON_ID";
    String LAYER_ID = "LAYER_ID";
    String selected_landmark;

    //Request Code
    private static final int DESTINATION_REQUEST_CODE = 56789;

    //Declaring Firebase Variables
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    //Initialising Classes
    PreferredLandmarkType preferredLandmarkType;
    FavouriteLandmarks favouriteLandmarks;
    UserSettings userSettings;

    //Declaring Mapping Layout Components Variables
    MapView mapView;
    MapboxMap mapboxMap;
    Button navigate_btn;
    FloatingActionButton favourite_btn, search_landmark_btn;
    PermissionsManager permissionsManager;
    LocationComponent locationComponent;
    DirectionsRoute route;
    CategorySearchEngine searchEngine;
    SearchRequestTask requestTask;

    //Direction Variables
    private static final String T_A_G = "Route_Activity";
    NavigationMapRoute mapRoute;
    Double latitude = 0.0;
    Double longitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting Mapbox Access Token Key
        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_view_map);

        //Assigning Declared Variables To Their Respective Hooks
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        favourite_btn = findViewById(R.id.favourite_landmarks_btn);
        search_landmark_btn = findViewById(R.id.favourite_search_landmarks_btn);
        navigate_btn = findViewById(R.id.navigate_btn);
        //Instantiating Firebase Declared Variables
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = mAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference(mAuth.getCurrentUser().getUid());




        //Initialising Map Box Search Sdk
         MapboxSearchSdk.initialize(this.getApplication(), getString(R.string.access_token),
         new DefaultLocationProvider(this.getApplication()));

        //Creating Search Engine Category
        searchEngine = MapboxSearchSdk.createCategorySearchEngine();

        //Declaring Search Category Options Variable
        CategorySearchOptions searchOptions = new CategorySearchOptions.Builder().limit(5).build();
        requestTask = searchEngine.search("Modern", searchOptions, searchCallback);


        //Creating On Click Listener For Floating Save Landmark Button
        favourite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogBox();

            }
        });
        navigate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean route_simulator = true;

                NavigationLauncherOptions navigationLauncherOptions = NavigationLauncherOptions.builder().directionsRoute(route).shouldSimulateRoute(route_simulator).build();

                NavigationLauncher.startNavigation(ViewMap.this, navigationLauncherOptions);
            }
        });

        //Checking If User Exists Or Are Stored Firebase Database
        databaseReference.child("settings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Retrieving Saved User Settings From Firebase
                userSettings = snapshot.getValue(UserSettings.class);

                if (userSettings != null) {

                    try {
                        if (userSettings.getUnit_setting().equals("Metric")) {

                            directions_criteria = "METRIC";
                        } else if (userSettings.getUnit_setting().equals("Imperial")) {

                            directions_criteria = "IMPERIAL";

                        } else if (userSettings.getTraffic().equals("true")) {

                            traffic_settings = true;

                        } else {
                            traffic_settings = false;

                        }

                    } catch (Exception e) {
                        Toast.makeText(ViewMap.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    //Setting Measurement System To Metric (KM)
                    directions_criteria = "METRIC";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ViewMap.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        databaseReference.child("landmarks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                favouriteLandmarks = snapshot.getValue(FavouriteLandmarks.class);
                if (favouriteLandmarks != null) {

                    selected_landmark = favouriteLandmarks.getPreferred_landmark();

                    //Exception Handling For
                    try {

                        if (selected_landmark != null && !selected_landmark.equals("All")) {

                            requestTask = searchEngine.search(selected_landmark, searchOptions, searchCallback);
                        }

                    } catch (Exception e) {

                        Toast.makeText(ViewMap.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewMap.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void DialogBox() {

        FavouritesDialog favouritesDialog = new FavouritesDialog();
        favouritesDialog.show(getSupportFragmentManager(), "favourite dialog");

    }

    public void applyTexts(String favourite_Landmark, String favorite_desc) {

        favourite_landmark = favourite_Landmark;
        favourite_landmark_desc = favorite_desc;
        try {
            //Passing Saved Favourite Location To Class For Database Storage
            preferredLandmarkType = new PreferredLandmarkType(favourite_landmark, favourite_landmark_desc, latitude, longitude);
            DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getCurrentUser().getUid());
            databaseReference.child("favourites").push().setValue(preferredLandmarkType).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Toast.makeText(ViewMap.this, "New Favourite Location Saved Successfully", Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ViewMap.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {

            Toast.makeText(ViewMap.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
        requestTask.cancel();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onExplanationNeeded(List<String> list) {

        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        Mapbox.getInstance(this, getString(R.string.access_token));

        Intent data = new Intent();
        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());


        latitude = destinationPoint.latitude();
        longitude = destinationPoint.longitude();

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        getRoute(originPoint, destinationPoint);

        navigate_btn.setEnabled(true);
        navigate_btn.setBackgroundResource(R.color.mapboxBlue);
        favourite_btn.setEnabled(true);
        favourite_btn.setBackgroundResource(R.color.coral);

        /** CODE ATTRIBUTION
         *  Places plugin for Android, mapbox.com.
         * https://docs.mapbox.com/android/plugins/guides/places/
         * **/
        Intent intent = new PlacePicker.IntentBuilder()
                .accessToken(Mapbox.getAccessToken())
                .placeOptions(
                        PlacePickerOptions.builder()
                                .statingCameraPosition(
                                        new CameraPosition.Builder()
                                                .target(new LatLng(destinationPoint.latitude(),destinationPoint.longitude()))
                                                .zoom(25)
                                                .build())
                                .build())
                .build(this);
        startActivityForResult(intent, DESTINATION_REQUEST_CODE);

        return true;



    }


    private void getRoute(Point origin, Point destination) {

        Mapbox.getInstance(this, getString(R.string.access_token));

        // use the metric measurement system
        if(directions_criteria == "METRIC")
        {
            NavigationRoute.builder(this)
                    .accessToken(Mapbox.getAccessToken())
                    .voiceUnits(DirectionsCriteria.METRIC)
                    .origin(origin)
                    .destination(destination)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>()
                    {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            Log.d(T_A_G, "Response code: " + response.code());
                            if (response.body() == null) {
                                Log.e(T_A_G, "No routes found, make sure you set the right user and access token.");
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Log.e(T_A_G, "No routes found");
                                return;
                            }

                            route = response.body().routes().get(0);

                            // Draw the route on the map
                            if (mapRoute != null) {
                                mapRoute.removeRoute();
                            } else {
                                mapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                            }
                            mapRoute.addRoute(route);
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                            Log.e(T_A_G, "Error: " + throwable.getMessage());
                        }
                    });
        }
        // using other (IMPERIAL) measurement system
        else
        {
            NavigationRoute.builder(this)
                    .accessToken(Mapbox.getAccessToken())
                    .voiceUnits(DirectionsCriteria.IMPERIAL)
                    .origin(origin)
                    .destination(destination)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>()
                    {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            Log.d(T_A_G, "Response code: " + response.code());
                            if (response.body() == null) {
                                Log.e(T_A_G, "No routes found, make sure you set the right user and access token.");
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Log.e(T_A_G, "No routes found");
                                return;
                            }

                            route = response.body().routes().get(0);

                            // Draw the route on the map
                            if (mapRoute != null) {
                                mapRoute.removeRoute();
                            } else {
                                mapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                            }
                            mapRoute.addRoute(route);
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                            Log.e(T_A_G, "Error: " + throwable.getMessage());
                        }
                    });
        }
    }


    private final SearchCallback searchCallback = new SearchCallback()
    {
        @Override
        public void onResults(@NonNull List<? extends SearchResult> results, @NonNull ResponseInfo responseInfo)

        {
            if (results.isEmpty())
            {
                Log.i("SearchApiExample", "No category search results");
            } else {
                Log.i("SearchApiExample", "Category search results: " + results);
            }
        }

        @Override
        public void onError(@NonNull Exception e)
        {
            Log.i("SearchApiExample", "Search error", e);
        }
    };


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {


        Mapbox.getInstance(this, getString(R.string.access_token));
        ViewMap.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                        addDestinationIconSymbolLayer(style);

                        mapboxMap.addOnMapClickListener(ViewMap.this);

                        /** CODE ATTRIBUTION
                         *  Traffic, mapbox.com.
                         * https://docs.mapbox.com/android/plugins/guides/traffic/
                         * **/
                        // adding a real time traffic layer to the map
                        TrafficPlugin trafficPlugin = new TrafficPlugin(mapView, mapboxMap, style);
                        trafficPlugin.setVisibility(traffic_settings);

                    }
                });

    }
    @SuppressLint("MissingPermission")
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        Mapbox.getInstance(this, getString(R.string.access_token));

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            locationComponent = mapboxMap.getLocationComponent();;
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {

        Mapbox.getInstance(this, getString(R.string.access_token));

        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), com.mapbox.mapboxsdk.places.R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { super.onActivityResult(requestCode, resultCode, data);

        Mapbox.getInstance(this, getString(R.string.access_token));

        if (requestCode == DESTINATION_REQUEST_CODE && resultCode == RESULT_OK){

            // Retrieve the information from the selected location's CarmenFeature

            CarmenFeature carmenFeature = PlacePicker.getPlace(data);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}