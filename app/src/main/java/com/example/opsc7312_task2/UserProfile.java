package com.example.opsc7312_task2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity {

    /*
     GLOBAL VARIABLE DECLARATIONS
     */

    //Declaring Variables That Will Hold Data Input By The User When Update Or Edit Request Is Called
    String first_name, last_name, email;

    //Creating UserLogin Class Object
    UserLogin userLogin;

    //Declaring Firebase Variables To Access Firebase Values
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    //Declaring Variables For XML Components
    TextInputLayout f_name, s_name, e_mail;
    Button update_Btn;
    ImageView back_Btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Assigning XML Components To Hooks
        f_name = findViewById(R.id.fname);
        s_name = findViewById(R.id.lname);
        update_Btn = findViewById(R.id.update_btn);
        f_name = findViewById(R.id.fname);
        back_Btn = findViewById(R.id.back_Btn);

        //Instantiating Firebase Variables
        /*
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(mAuth.getCurrentUser().getUid());
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
*/

        //Creating On Click For When Update Button Is Clicked
 /*       update_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Calling Method To Change User Details
                updateDetails();
            }
        });

    }   */

    }
}