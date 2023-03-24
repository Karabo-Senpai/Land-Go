package com.example.opsc7312_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PreferredLandmarks extends AppCompatActivity {

    /*
     Global Variable Declarations

     */
    //String Variable To Hold Selected Preferred Landmark Type
    String selected_preferred_type;

    //Declaring Firebase Components Variables
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    //Instantiating Preferred Landmark  Class
    FavouriteLandmarks favouriteLandmarks;

    //Declaring Radio Button Variables
    RadioGroup preferred_radio_group;
    RadioButton all, modern, popular, historic, grafitti, park, statue, other, selected_type;

    //Declaring Button To Save Selected Landmark Type And Clickable Image To Navigate To The Previous Screen That Was Open
    Button save_btn;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_landmarks);

        //Instantiating Firebase Declared Variables
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getCurrentUser().getUid());

        //Assigning Hooks To Respective XML Components
        preferred_radio_group = findViewById(R.id.radioGroupLandmarks);
        all = findViewById(R.id.radioAll);
        modern = findViewById(R.id.radioModern);
        popular = findViewById(R.id.radioPopular);
        historic = findViewById(R.id.radioHistorical);
        grafitti = findViewById(R.id.radioGrafitti);
        park = findViewById(R.id.radioParks);
        statue = findViewById(R.id.radioStatues);
        other = findViewById(R.id.radioOther);
        save_btn = findViewById(R.id.save_btn);
        back_btn = findViewById(R.id.back_btn);


        //Checking If The User Has A Preferred Landmark Saved In The Database
        databaseReference.child("landmark").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                favouriteLandmarks = snapshot.getValue(FavouriteLandmarks.class);

                if (favouriteLandmarks != null) {

                    try {

                        //Decision Statement To Get Preferred Landmark Type
                        switch (favouriteLandmarks.getPreferred_landmark()) {

                            case "All":
                                all.setChecked(true);
                                break;

                            case "Modern":
                                modern.setChecked(true);
                                break;

                            case "Popular":
                                popular.setChecked(true);
                                break;

                            case "Historical":
                                historic.setChecked(true);
                                break;

                            case "Grafitti":
                                grafitti.setChecked(true);
                                break;

                            case "Parks":
                                park.setChecked(true);
                                break;

                            case "Statues":
                                statue.setChecked(true);
                                break;

                            case "Other":
                                other.setChecked(true);
                                break;
                        }

                    } catch (Exception e) {
                        Toast.makeText(PreferredLandmarks.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(PreferredLandmarks.this, "Please Save Your Selected Landmark Type", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //Error Message To Be Displayed When Preferred Type Is Not Saved \
                Toast.makeText(PreferredLandmarks.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        //Setting Onclick Event For When Save Button Is Clicked
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int preferred_landmark = preferred_radio_group.getCheckedRadioButtonId();
                selected_type = (RadioButton) findViewById(preferred_landmark);

                //Checking If Preferred Landmark Has Not Been Checked
                if (preferred_landmark == -1) {
                    Toast.makeText(PreferredLandmarks.this, "Preferred Landmark Has Been Selected!", Toast.LENGTH_SHORT).show();
                } else {

                    if (all.isChecked()) {

                        selected_preferred_type = "All";
                    } 
                    else if (modern.isChecked()) {
                        selected_preferred_type = "Modern";
                    } 
                    else if (popular.isChecked()) {
                        selected_preferred_type = "Popular";
                    } 
                    else if (historic.isChecked()) {
                        selected_preferred_type = "Historical";
                    } 
                    else if (grafitti.isChecked()) {
                        selected_preferred_type = "Grafitti";
                    } 
                    else if (park.isChecked()) {
                        selected_preferred_type = "Parks";
                    }
                    else if (statue.isChecked()) {
                        selected_preferred_type = "Statues";
                    }
                    else if (other.isChecked()) {
                        selected_preferred_type = "Other";
                    }

                    //Passing Selected Landmark Type To The Class
                    favouriteLandmarks = new FavouriteLandmarks(selected_preferred_type);
                    
                    DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getCurrentUser().getUid());
                    databaseReference.child("landmarks").setValue(favouriteLandmarks).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(PreferredLandmarks.this, "Preferred Landmark Successfully Saved", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            

                            Toast.makeText(PreferredLandmarks.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    
                }
            }
        });
    }
}