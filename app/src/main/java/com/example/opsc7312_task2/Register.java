package com.example.opsc7312_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    //Initialising Variables For XML Components
    TextInputLayout fName, lName, email, userName, password, conpass;

    //Button To Redirect User To LogIn If They Already Have An Account
    Button logIn, regBtn;

    //Adding Authentication Method Variable
    private FirebaseAuth mAuth;

    //Instantiating User Login Class
    UserLogin userLogins;

    //Create Variable For Image View Component
    ImageView imageView;
    TextView logoname;

    //Creating Global Variable For Firebase Database and Database Reference
    FirebaseDatabase database;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Calling Firebase Database
        database = FirebaseDatabase.getInstance();

        //Referencing Which Table Or Attribute To Insert The Data Into
        databaseReference = database.getReference("users");

        mAuth = FirebaseAuth.getInstance();

        //Assigning Values To Declared Variables
        fName = findViewById(R.id.fname);
        lName = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        conpass = findViewById(R.id.conpass);
        regBtn = findViewById(R.id.regBtn);
        logIn = findViewById(R.id.login);

        //Moving User To Log In Screen If They Have An Account
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);

                //Creating Pairs For the Shared Animation Code
                Pair[] pair = new Pair[6];

                //Assigning Animation Pairs To Respective XML Components
                pair[0] = new Pair<View, String>(imageView, "logo");
                pair[1] = new Pair<View, String>(logoname, "logo_text");
                pair[2] = new Pair<View, String>(email, "email_trans");
                pair[3] = new Pair<View, String>(password, "pass_trans");
                pair[4] = new Pair<View, String>(logIn, "btn_Trans");
                pair[5] = new Pair<View, String>(regBtn, "reg_trans");

                //Calling Next Activity
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Register.this, pair);
                startActivity(i, options.toBundle());

            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!validateName() | !validateSurname() | !validateEmail() | !validatePassword() | !validateConPass()) {

                    return;
                }

                        else {
                    //Getting Data That Was Inputted By The User In The Register Screen Form Before Passing It Into The Database
                    String name = fName.getEditText().getText().toString();
                    String sname = lName.getEditText().getText().toString();
                    String emailAdd = email.getEditText().getText().toString();
                    String pass = password.getEditText().getText().toString();
                    String cpass = conpass.getEditText().getText().toString();

                    //Assigning Input
                    userLogins = new UserLogin(name, sname, emailAdd);

                    if (pass.equals(cpass)) {

                        mAuth.createUserWithEmailAndPassword(emailAdd, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    databaseReference.push().setValue(userLogins);

                                    //Confirmation Message To User That Their Registration Was A Success
                                    Toast.makeText(Register.this, "User" + " " + mAuth.getCurrentUser().getEmail() + " Has Been Successfully Registered", Toast.LENGTH_SHORT).show();

                                    //Moving User To The Login Screen

                                    Intent intent = new Intent(Register.this, Login.class);

                                    intent.putExtra("First Name", name);
                                    intent.putExtra("Surname", sname);

                                    //Starting The Login Page
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(Register.this, "Password And Confirm Password Do Not Match",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    else{ Toast.makeText(Register.this, "Password And Confirm Password Do Not Match",
                            Toast.LENGTH_SHORT).show();}
                }

            }
        });

    }


    //Validating First Name Field
   private Boolean validateName() {

        String valName = fName.getEditText().getText().toString();

        if (valName.isEmpty()) {

            fName.setError("Firstname Cannot Be Left Empty!");
            return false;
        } else {
            fName.setError(null);
            fName.setErrorEnabled(false);
            return true;
        }
    }

    //Validating  Last Name Field
    private Boolean validateSurname() {

        String valName = lName.getEditText().getText().toString();

        if (valName.isEmpty()) {

            lName.setError("Lastname Cannot Be Left Empty!");
            return false;
        } else {
            lName.setError(null);
            lName.setErrorEnabled(false);
            return true;
        }
    }

    //Validating Email Address User has Inputted To Check if Its Valid Or Not
    private Boolean validateEmail() {

        String valName = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (valName.isEmpty()) {

            email.setError("Email cannot Be Left Empty!");
            return false;
        } else if (!valName.matches(emailPattern)) {

            email.setError("Enter A Valid Email Address!");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    //Validating User Password
    private Boolean validatePassword() {

        //Variable To Check Validations
        String valName = password.getEditText().getText().toString();

        //Regex Pattern For Password Complexity
        String passwordPattern = ("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$");

        //Checking If Password Field Is Empty Or Not
        if (valName.isEmpty()) {

            password.setError("Password cannot Be Left Empty!");
            return false;
        }
        else if (!valName.matches(passwordPattern)) {

            password.setError("Password Is Too Weak!");
            return false;
        }
        //Checking Password Length
        else if (valName.length() < 8)
        {
            password.setError("Password Should Be 8 Characters Long!");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    //Validating User Password
    private Boolean validateConPass() {

        //Variable To Check Validations
        String valName = conpass.getEditText().getText().toString();



        //Checking If Confirm Password Field Is Empty Or Not
        if (valName.isEmpty()) {

            conpass.setError("Confirm Password Cannot Be Left Empty!");
            return false;
        }
        //Checking Password Length
        else if (valName.length() < 8)
        {
            conpass.setError("Password Should Be 8 Characters Long!");
            return false;
        }
        else{
            conpass.setError(null);
            conpass.setErrorEnabled(false);
            return true;
        }
    }



}