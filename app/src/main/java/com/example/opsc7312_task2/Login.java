package com.example.opsc7312_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    //Initialising Variables For XML Components
    ImageView imageView;
    TextView logoname;
    TextInputLayout email, password;

    //Declaring ProgressDialog Variable
    ProgressDialog progressDialog;

    //Creating Variable For Register Button For Some Action To Be Performed When Pressed
    Button register;

    //Variable For LogIn Button
    Button login, forgot_pass;

    //Setting Up Firebase Authentication
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    //Creating Shared Preference Variables
    SharedPreferences onBoardScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialising Declared Firebase Authentication Variables
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


       //Assigning Respective XML Components To Declared Variables
        register = findViewById(R.id.reg);
        imageView = findViewById(R.id.logo_img);
        logoname = findViewById(R.id.logo_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.logBtn);
        forgot_pass = findViewById(R.id.forgot);


  register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

          //Creating Pairs For the Shared Animation Code
          Pair[] pair = new Pair[7];


          //Assigning Animation Pairs To Respective XML Components
          pair[0] = new Pair<View, String>(imageView, "logo");
          pair[1] = new Pair<View, String>(logoname, "logo_text");
          pair[2] = new Pair<View, String>(email, "email_trans");
          pair[3] = new Pair<View, String>(password, "pass_trans");
          pair[4] = new Pair<View, String>(login, "btn_Trans");
          pair[5] = new Pair<View, String>(register, "reg_trans");
          pair[6] = new Pair<View,String>(forgot_pass,"forpass_trans");

          Intent i = new Intent(Login.this,Register.class);
         ;

          ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pair);
          startActivity(i, options.toBundle());
      }
  });

  login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {


          onBoardScreen = getSharedPreferences("onBoardScreen", MODE_PRIVATE);

          //Creating Boolean Variable To Check If Its The Users First Time Using The Application
          boolean IsFirstTime = onBoardScreen.getBoolean("firstTimeUse",true);

          if (!validateEmail() | !validatePassword()) {

              return;
          } else {


              String inputEmail = email.getEditText().getText().toString().trim();
              String inputPassword = password.getEditText().getText().toString().trim();

              mAuth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {

                      //Condition For When Sign In Is Successful
                      if (task.isSuccessful()) {

                          Toast.makeText(Login.this, " " + mAuth.getCurrentUser().getEmail() + " " + "Has Successfully Logged In ", Toast.LENGTH_SHORT).show();

                          if(IsFirstTime){

                              SharedPreferences.Editor editor = onBoardScreen.edit();
                              editor.putBoolean("firstTimeUse",false);
                              editor.commit();

                              Intent intent = new Intent(getApplicationContext(),Onboard.class);
                              startActivity(intent);
                              finish();
                          }
                          else{

                              Intent intent = new Intent(getApplicationContext(),HomeMain.class);
                              startActivity(intent);
                              finish();
                          }

                      }
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {

                      //Toast To Show User Why The Log In Failed
                      Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                  }
              });

          }
      }
  });

    }

    @Override
    public void onBackPressed() {

        //Dismissing Progress Bar After Load Done
        progressDialog.dismiss();
    }

    private Boolean validateEmail() {

        String valName = email.getEditText().getText().toString();

        if (valName.isEmpty()) {

            email.setError("Email cannot Be Left Empty!");
            return false;
        }

        //
        else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    //Validating User Password
    private Boolean validatePassword() {

        //Variable To Check Validations
        String valName = password.getEditText().getText().toString();

        //Checking If Password Field Is Empty Or Not
        if (valName.isEmpty()) {

            password.setError("Password cannot Be Left Empty!");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

}