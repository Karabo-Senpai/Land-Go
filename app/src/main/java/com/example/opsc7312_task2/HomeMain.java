package com.example.opsc7312_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.opsc7312_task2.Adapters.HomeAdapter.CategoryAdapter;
import com.example.opsc7312_task2.Adapters.HomeAdapter.CategoryHelper;
import com.example.opsc7312_task2.Adapters.HomeAdapter.FeaturedAdapter;
import com.example.opsc7312_task2.Adapters.HomeAdapter.FeaturedHelper;
import com.example.opsc7312_task2.Adapters.HomeAdapter.PointsAdapter;
import com.example.opsc7312_task2.Adapters.HomeAdapter.PointsHelper;
import com.example.opsc7312_task2.HelperClasses.IntentHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*
       GLOBAL VARIABLE DECLARATIONS!
     */

    //
    static final float END_SCALE = 0.7f;

    //Alert Dialog
    AlertDialog alertDialog;

    //Creating Firebase Variables
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    FirebaseUser current_user;

    //Creating Variables For XML Hook Components
    RecyclerView recyclerView, recyclerView2, recyclerView3;
    RecyclerView.Adapter adapter;
    ImageView menu_icon;
    //Creating Hooks For XML Drawer Layout Component
    DrawerLayout drawerLayout;

    //Creating Hooks For XML Navigation View Component
    NavigationView navigationView;

    //Creating Hooks For XML Linear Layout View Component
    LinearLayout contentView;

    //Instantiating Class
    IntentHelper intentHelper = new IntentHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        //Assigning Declared Variables To Their Respective Hooks
        recyclerView = findViewById(R.id.featuredland_recycler);
        recyclerView2 = findViewById(R.id.points_recycler);
        recyclerView3 = findViewById(R.id.categories_recycler);
        menu_icon = findViewById(R.id.nav_menu_icon);
        contentView = findViewById(R.id.content);

        //Assigning Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        //Assigning Declared Variables
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();


        //Calling Method To Open Navigation Menu
        NavigationDrawer();

        //Calling Method For Featured Landmarks Recyclerview
        featuredLandmarks();

        //Calling Method For Points Of Interest Recyclerview
        pointsOfInterest();

        LandmarkCategories();
    }

    private void NavigationDrawer() {

        //Making Navigation View Clickable
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        //Setting On Click Listener For Menu Icon
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {

                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {

                    drawerLayout.openDrawer(GravityCompat.START);
                }


            }
        });
        AnimateNavigationDrawer();
    }

    //Method To Animate Navigation Drawer
    private void AnimateNavigationDrawer() {

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scaling Navigation View Based On Slide Offset in Current Effect
                final float diff_Scaled_Offset = slideOffset * (1 - END_SCALE);
                final float offset_Scale = 1 - diff_Scaled_Offset;
                contentView.setScaleX(offset_Scale);
                contentView.setScaleY(offset_Scale);

                //Translating View Based On Device Scaled Width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diff_Scaled_Offset / 2;
                final float x_translation = xOffset - xOffsetDiff;
                contentView.setTranslationX(x_translation);

            }

        });

    }

    //Method To Close Navigation Menu Instead Of Application
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Setting Navigation Menu Screen Changing
        switch (item.getItemId()) {
            //Directing User To The Home Screen
            case R.id.nav_home:
                intentHelper.Nav_Open_Intent(this, HomeMain.class);
                break;
            //Case Should The User Want To Logout of The Application
            case R.id.nav_logout:
                Toast.makeText(this, " " + mAuth.getCurrentUser().getEmail() + "Has Successfully Logged Out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                finishAffinity();
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;

            case R.id.nav_settings:
                intentHelper.Nav_Open_Intent(this, Settings.class);
                break;

            case R.id.nav_preferred_landmarks:
                intentHelper.Nav_Open_Intent(this, PreferredLandmarks.class);
                break;

            case R.id.nav_map:
                intentHelper.Nav_Open_Intent(this, ViewMap.class);
                break;

            case R.id.nav_search:
                intentHelper.Nav_Open_Intent(this, SearchLandmark.class);
                break;

            case R.id.nav_saved_landmarks:
                intentHelper.Nav_Open_Intent(this, Saved_Favorite_Landmarks.class);
                break;

            case R.id.nav_profile:
                intentHelper.Nav_Open_Intent(this, UserProfile.class);
                break;
        }
        return true;
    }

    //Method To Show Featured Landmarks In The Recycler View
    private void featuredLandmarks() {

        //Setting Recycler View To Show Content That is Visible To The User
        recyclerView.setHasFixedSize(true);

        //Creating Recycler View Layout Orientation
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Passing Array To The Design Adapter
        ArrayList<FeaturedHelper> featuredLandmarks = new ArrayList<>();

        //Adding Data To Array List
        featuredLandmarks.add(new FeaturedHelper(R.drawable.bigbird, "Big Bird By ROA", "20 Kruger Street, Maboneng"));
        featuredLandmarks.add(new FeaturedHelper(R.drawable.gandhi, "Gandhi Square", "New St,Marshalltown,Johannesburg, 2001"));
        featuredLandmarks.add(new FeaturedHelper(R.drawable.community_chalkboard, "Community Chalkboard", "249 Fox Street, Maboneng"));
        featuredLandmarks.add(new FeaturedHelper(R.drawable.unionbuilding, "Union Buildings", "Government Ave,Pretoria,0022"));
        //Passing Array List To Adapter
        adapter = new FeaturedAdapter(featuredLandmarks);
        //
        recyclerView.setAdapter(adapter);
    }

    private void pointsOfInterest() {

        //Setting Recycler View To Show Content That is Visible To The User
        recyclerView2.setHasFixedSize(true);

        //Creating Recycler View Layout Orientation
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Passing Array To The Design Adapter
        ArrayList<PointsHelper> pointsHelpers = new ArrayList<>();

        //Adding Data To Array List
        pointsHelpers.add(new PointsHelper(R.drawable.bighole, "Kimberly Big Hole", "S Circular Rd, Kimberley, 8300"));
        pointsHelpers.add(new PointsHelper(R.drawable.addoelephant, "Addo Elephant Park", "Addo Elephant National Park, Addo, 6105"));
        pointsHelpers.add(new PointsHelper(R.drawable.graffiti2, "Zebra Graffiti", "Gandhi Square"));
        pointsHelpers.add(new PointsHelper(R.drawable.tablemountain, "Table Mountain", "Lower Cableway Station, Tafelberg Road, Cape Town"));

        //Passing Array List To Adapter
        adapter = new PointsAdapter(pointsHelpers);

        //
        recyclerView2.setAdapter(adapter);

    }


    //Method To Show Categories Recycler View
    private void LandmarkCategories() {

        //Setting Recycler View To Show Content That is Visible To The User
        recyclerView3.setHasFixedSize(true);

        //Creating Recycler View Layout Orientation
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Passing Array To The Design Adapter
        ArrayList<CategoryHelper> categoryHelpers = new ArrayList<>();

        //Adding Data To Array List
        categoryHelpers.add(new CategoryHelper(R.drawable.cradle, "Historical"));
        categoryHelpers.add(new CategoryHelper(R.drawable.kgalagadi, "Park"));
        categoryHelpers.add(new CategoryHelper(R.drawable.graffiti1, "Graffiti"));
        categoryHelpers.add(new CategoryHelper(R.drawable.unionbuilding, "Popular"));
        categoryHelpers.add(new CategoryHelper(R.drawable.bosjes_chapel, "Modern"));

        //Passing Array List To Adapter
        adapter = new CategoryAdapter(categoryHelpers);

        //Setting Recycler View To Adapter
        recyclerView3.setAdapter(adapter);

    }

}