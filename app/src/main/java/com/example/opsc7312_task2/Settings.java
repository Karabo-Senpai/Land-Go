package com.example.opsc7312_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {

    /*
      GLOBAL VARIABLE DECLARATION

     */
    String measurement_units, traffic, selected_item;

    //Declaring Firebase Variables To Store Settings Into Database Depending On Signed In User
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    RadioButton measure_metric_btn, measure_imperial_btn, interface_btn, checked_btn;

    //
    Button save_btn;
    ImageView back_btn;
    SwitchMaterial switchMaterial;
    RadioGroup measure_group, interface_group;
    UserSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Setting Up Database Components
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference(mAuth.getCurrentUser().getUid());

        //Assigning Hooks
        save_btn = findViewById(R.id.save_btn);
        back_btn = findViewById(R.id.back_icon);
        measure_metric_btn = findViewById(R.id.radioMetric);
        measure_imperial_btn = findViewById(R.id.radioImperial);
        measure_group = findViewById(R.id.measurement_units_radio_group);
        switchMaterial = findViewById(R.id.traffic_switch_view);


        //Setting Onclick Events For Save Button
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selected_item_id = measure_group.getCheckedRadioButtonId();

                checked_btn = (RadioButton) findViewById(selected_item_id);

                if (selected_item_id == -1) {
                    Toast.makeText(Settings.this, "Measurement System Not Selected", Toast.LENGTH_SHORT).show();
                } else {


                    try {

                        if (measure_metric_btn.isChecked()) {

                            measurement_units = "Metric";
                            //  Toast.makeText(Settings.this, "Metric System Selected", Toast.LENGTH_SHORT).show();
                        } else {

                            measurement_units = "Imperial";
                            // Toast.makeText(Settings.this, "Imperial System Selected", Toast.LENGTH_SHORT).show();

                        }

                        settings = new UserSettings(measurement_units, traffic);
                        DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getCurrentUser().getUid());

                        databaseReference.child("settings").setValue(settings).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(Settings.this, "Settings Successfully Saved", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Settings.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(Settings.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        //Button To Go Back To Screen That The App Was On Before The Settings Screen Was Called
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // startActivity(new Intent(getApplicationContext(),));
            }
        });

        //Setting OnSelected Method For When An Item Is Selected
        databaseReference.child("settings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                settings = snapshot.getValue(UserSettings.class);
                if (settings != null) {

                    try {

                        if (settings.getUnit_setting().equals("Metric")) {

                            measure_metric_btn.setChecked(true);

                        } else if (settings.getUnit_setting().equals("Imperial")) {

                            measure_imperial_btn.setChecked(true);
                        }

                        if (settings.getTraffic().equals("true")) {

                            switchMaterial.setChecked(true);
                        } else {

                            switchMaterial.setChecked(true);
                        }

                    } catch (Exception e) {
                        Toast.makeText(Settings.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    //  Toast.makeText(Settings.this, "You Might Want To Save Your Selection", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        //Alert Switch Material View
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_toggled) {

                //Checking If The Toggle Has Been Tapped / Clicked
                if (is_toggled) {

                    //Switch Material View Toggle Is Enabled
                    traffic = "true";

                    // Toast.makeText(Settings.this, "Traffic Alerts Are Now On", Toast.LENGTH_SHORT).show();
                } else {
                    traffic = "false";
                    //  Toast.makeText(Settings.this, "Traffic Alerts Are Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}