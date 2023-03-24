package com.example.opsc7312_task2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Global Variable Declarations
    Animation topAnimation, bottomAnimation;

    //Creating Variable For ImageView To Set Animations
    ImageView imageView;

    TextView devStudio1;

    //Creating Shared Preference Variables
    //SharedPreferences onBoardScreen;

    //Creating Static Variable To Time Move From Splash Screen To Login Screen
    private static int SPLASH_SCREEN = 3500;

    //Creating Textview Variables To Set Animations
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hiding Status Bar9
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assigning Declared Variables
        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Assigning Declared Variables With Fields
        devStudio1 = findViewById(R.id.logoText1);
        imageView = findViewById(R.id.app_logo);

        //Assigning Image And Text Animations
        imageView.setAnimation(topAnimation);
        devStudio1.setAnimation(bottomAnimation)
        ;
        //Creating Intent To Call Splash Screen Activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Invoking Next Activity
                Intent intent = new Intent(MainActivity.this , Login.class);

                //Creating Pair For Shared Animation Preferences (Animations Will Be Carried Over To The Next Screen)
                Pair[] pair = new Pair[2];

                //Assigning Transition Values To Pairs For Shared Preference
                pair[0] = new Pair<View,String> (imageView,"logo");
                pair[1] = new Pair<View,String> (devStudio1,"logo_text");


                //Syncing Or Binding Animations To Sign In Screen
                ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pair);

               //Passing  Animations To Intent
               startActivity(intent,activityOptions.toBundle());

               //Ending Animations After Transition
                finish();
            }
        },SPLASH_SCREEN);


    }
}