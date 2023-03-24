package com.example.opsc7312_task2;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchLandmark extends AppCompatActivity implements OnMapReadyCallback, Callback<DirectionsResponse>, PermissionsListener {

    /*
     Global Variable Declarations
     */

    //Mapbox Variable Declarations
    MapView mapView;
    public MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private static final int REQUEST_CODE_COMPLETE = 1;
    private static final int REQUEST_CODE = 5678;
    private static final String ROUTE_LAYER_ID = "route_layer_id";
    private static final String ROUTE_SOURCE_ID = "route_source_id";
    private static final String ICON_LAYER_ID = "icon_layer_id";
    private static final String ICON_SOURCE_ID = "icon_source_id";
    private static final String RED_PIN_ICON_ID = "red_pin_icon_id";
    public String address;
    private CarmenFeature home;
    private CarmenFeature work;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";
    Point origin = Point.fromLngLat(28.218370, -25.731340);
    Point destination = Point.fromLngLat(28.218370, -25.731340);
    private MapboxDirections client;
    int c = 0;
    MapboxNavigation navigation;
    double distance;
    Button nav_btn;
    String startLocation = "";
    String endLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_search_landmark);

        navigation = new MapboxNavigation(this, getString(R.string.access_token));
        mapView = (MapView) findViewById(R.id.nav_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        nav_btn = findViewById(R.id.navigate_btn);

        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navigationRoute();
            }
        });


    }

    @Override
    public void onExplanationNeeded(List<String> list) {

    }

    @Override
    public void onPermissionResult(boolean granted) {


        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });

        } else {
            finish();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onMapReady(final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                //Method To Show Users Location
                enableLocationComponent(style);

                //Method To Invoke Location / Landmark Search
                initSearchFab();

                //Method To Put Default Location
                addUserLocation();


                //Setting Location Marker
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_location_on_24, null);
                Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);
                style.addImage(symbolIconId, bitmap);

                //Creating Geojson Source
                setUpSource(style);

                setUpLayer(style);

                initSource(style);
                initlayers(style);

                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {

                    LatLng source;

                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {

                        //
                        if (c == 0) {

                            origin = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                            source = point;
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(point);
                            markerOptions.title("Source");
                            mapboxMap.addMarker(markerOptions);
                            reverseGeocodeFunc(point, c);
                        }
                        if (c == 1) {
                            destination = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                            getRoute(mapboxMap, origin, destination);
                            MarkerOptions options = new MarkerOptions();
                            options.position(point);
                            options.title("destination");
                            mapboxMap.addMarker(options);
                            reverseGeocodeFunc(point, c);
                            getRoute(mapboxMap, origin, destination);

                        }

                        if (c > 1) {
                            c = 0;
                            recreate();
                        }
                        c++;

                        return true;
                    }
                });
            }
        });
    }

    private void reverseGeocodeFunc(LatLng point, int c) {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder().accessToken(getString(R.string.access_token)).query(Point.fromLngLat(point.getLongitude(), point.getLatitude())).geocodingTypes(GeocodingCriteria.TYPE_POI_LANDMARK).build();
        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                List<CarmenFeature> results = response.body().features();

                if (results.size() > 0) {
                    CarmenFeature feature;
                    Point firstResultPoint = results.get(0).center();
                    feature = results.get(0);
                    if (c == 0) {

                        startLocation += feature.placeName();
                    }
                    if (c == 1) {
                        endLocation += feature.placeName();

                    }
                } else {
                    Toast.makeText(SearchLandmark.this, "Landmark Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {

                t.printStackTrace();
            }
        });

    }

    private void initlayers(@NonNull Style loadedMapStyle) {

        LineLayer route_layer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        route_layer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND), PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(5f),
                PropertyFactory.lineColor(Color.parseColor("#ff7f50"))
        );
        loadedMapStyle.addLayer(route_layer);

        //Adding Marker To The Map
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(com.mapbox.mapboxsdk.R.drawable.mapbox_marker_icon_default)));

        //
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(iconImage(RED_PIN_ICON_ID),
                PropertyFactory.iconIgnorePlacement(true), PropertyFactory.iconAllowOverlap(true),
                iconOffset(new Float[]{0f, -9f})));
    }


    private void initSource(@NonNull Style loadedMapStyle) {

        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));
        GeoJsonSource geoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));

        loadedMapStyle.addSource(geoJsonSource);
    }

    private void getRoute(final MapboxMap mapboxMap, Point origin, Point destination) {

        client = MapboxDirections.builder().origin(origin).destination(destination).overview(DirectionsCriteria.OVERVIEW_FULL).profile(DirectionsCriteria.PROFILE_DRIVING).accessToken(getString(R.string.access_token)).build();

        client.enqueueCall(this);
    }

    private void navigationRoute() {
        NavigationRoute.builder(this).accessToken(getString(R.string.access_token)).origin(origin).destination(destination).build().getRoute(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {

                    Toast.makeText(SearchLandmark.this, "No Navigation Route Found Make Sure To Set Right User And Access", Toast.LENGTH_SHORT).show();
                    return;
                } else if (response.body().routes().size() < 1) {

                    Toast.makeText(SearchLandmark.this, "No Navigation Route Found", Toast.LENGTH_SHORT).show();
                }

                DirectionsRoute route = response.body().routes().get(0);
                boolean simulateRoute = true;

                //Navigation Option
                NavigationLauncherOptions options = NavigationLauncherOptions.builder().directionsRoute(route).shouldSimulateRoute(simulateRoute).build();

                NavigationLauncher.startNavigation(SearchLandmark.this, options);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

        if (response.body() == null) {

            Toast.makeText(this, "Set Access And User Tokens Correctly", Toast.LENGTH_SHORT).show();
            return;
        } else if (response.body().routes().size() == 1) {

            Toast.makeText(this, "No Navigation Route Found", Toast.LENGTH_SHORT).show();
        }

        final DirectionsRoute currentRoute = response.body().routes().get(0);

        distance = currentRoute.distance() / 1000;


        if (mapboxMap != null) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {


                    GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

                    if (source != null) {
                        source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), Constants.PRECISION_5));
                    }

                }
            });
        }
    }
/*
    public void confirmed(View view) {

        navigationRoute();
    }   */

    private void initSearchFab() {

        findViewById(R.id.favourite_search_landmarks_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new PlaceAutocomplete.IntentBuilder().accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.access_token)).
                        placeOptions(PlaceOptions.builder().backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10).addInjectedFeature(home).addInjectedFeature(work).build(PlaceOptions.MODE_CARDS)).build(SearchLandmark.this);
                startActivityForResult(intent, REQUEST_CODE_COMPLETE);

            }
        });
    }

    private void addUserLocation() {
        home = CarmenFeature.builder().text("Mapbox SF Office").geometry(Point.fromLngLat(-122.3964485, 37.7912561)).
                placeName("50 Beale St, San Francisco,CA").id("mapbox-sf").properties(new JsonObject()).build();

        work = CarmenFeature.builder().text("Mapbox DC Office").geometry(Point.fromLngLat(-77.0338348, 38.899750)).
                placeName("740 15th Street NW, Washington DC").id("mapbox-dc").properties(new JsonObject()).build();

    }

    private void setUpSource(@NonNull Style loadedMapStyle) {

        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    private void setUpLayer(@NonNull Style loadedMapStyle) {

        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(iconImage(symbolIconId), iconOffset(new Float[]{0f, -8f})));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_COMPLETE) {

            CarmenFeature carmenFeature = PlaceAutocomplete.getPlace(data);

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{Feature.fromJson(carmenFeature.toJson())}));
                    }
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(((Point) carmenFeature.geometry()).latitude(), ((Point) carmenFeature.geometry()).longitude())).zoom(12).build()), 4000);

                }
            }

        }
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(SearchLandmark.this)) {

            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(SearchLandmark.this, loadedMapStyle).build());

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);

        }

    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

    }


}