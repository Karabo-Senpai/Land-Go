package com.example.opsc7312_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.opsc7312_task2.Adapters.SavedFavouriteLandmarksAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Saved_Favorite_Landmarks extends AppCompatActivity {

    /*
 GLOBAL VARIABLE DECLARATIONS
*/


    //Declaring Firebase Database Variables
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    PreferredLandmarkType preferredLandmarkType, items;

    //Declaring Saved Item Details Variables
    String landmark_name, landmark_desc;
    Double latitude, longitude;

    //Declaring List View And Recycler View
    List<PreferredLandmarkType> preferredLandmarkTypeList;
    RecyclerView recyclerView;
    RecyclerView.Adapter recycler_adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_favorite_landmarks);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference(mAuth.getCurrentUser().getUid());

        //Creating Array List Variable To Hold Favourite List items
        ArrayList<PreferredLandmarkType> landmarkTypeArrayList = new ArrayList<>();

         layoutManager = new LinearLayoutManager(this);

         databaseReference.child("favourites").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                 preferredLandmarkType = new PreferredLandmarkType();

                 for (DataSnapshot savedItems : snapshot.getChildren()){

                     preferredLandmarkType = savedItems.getValue(PreferredLandmarkType.class);
                     assert preferredLandmarkType != null;

                     items = new PreferredLandmarkType(preferredLandmarkType.landmark_name,preferredLandmarkType.description,preferredLandmarkType.latitude,preferredLandmarkType.longitude);
                     landmarkTypeArrayList.add(items);

                 }

                 //Try And Catch To Handle Any Error That May Occur
                 try {
                     recyclerView = findViewById(R.id.favorites_recycler);
                     recyclerView.setHasFixedSize(true);
                     Collections.reverse(landmarkTypeArrayList);
                     recycler_adapter = new SavedFavouriteLandmarksAdapter(landmarkTypeArrayList,Saved_Favorite_Landmarks.this);
                     recyclerView.setLayoutManager(layoutManager);
                     recyclerView.setAdapter(recycler_adapter);

                 }
                 catch(Exception e){
                     Toast.makeText(Saved_Favorite_Landmarks.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

                 Toast.makeText(Saved_Favorite_Landmarks.this, error.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });


    }
}