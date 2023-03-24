package com.example.opsc7312_task2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.opsc7312_task2.Adapters.SliderAdapter;

public class Onboard extends AppCompatActivity {

    //Creating XML Hooks
    ViewPager viewPager2;
    LinearLayout dotsLayout;
    Button get_started_btn;
    //Instantiating Slider Adapter Class
    SliderAdapter sliderAdapter;

    //Declaring Variable To Get The Current Slide Location
    int currentSlide;

    //Placeholder Textview to create slider motion dots
    TextView[] dots ;

    //Creating Animation Hook Variable
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);

        //Assigning Declared Variables To Respective XML Hooks
        viewPager2 = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
       get_started_btn = findViewById(R.id.get_startedBtn);

        //Calling Class
        sliderAdapter = new SliderAdapter(this);

        viewPager2.setAdapter(sliderAdapter);

        //Invoking Add Dots Function
        AddDots(0);

        viewPager2.addOnPageChangeListener(onPageChangeListener);

    }

    //Method To Skip Walk through
    public void Skip(View view){

        startActivity(new Intent(this,HomeMain.class));
        finish();
    }

    //Method To Go To The Next Slide
    public void NextSlide(View view){

        //Jumping To The Next Slide
        viewPager2.setCurrentItem(currentSlide+1);

    }
    //Creating Method To Create Dots To Show Sliding Motion
    private  void AddDots(int position){

        //Creating Dots Via The TextView Place Holder
        dots = new TextView[5];

        //Clearing Dots Layout
        dotsLayout.removeAllViews();
        //Using For Loop To Create Text Views
        for (int i = 0; i < dots.length; i++) {

            //Getting Current Position Of The Dots
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));

            dots[i].setTextSize(35);
           // dots[i].setTextColor(@);
            //Adding Dots To The Layout
            dotsLayout.addView(dots[i]);
        }

        //Checking If Dots Have Something
        if(dots.length > 0){

            dots[position].setTextColor(getResources().getColor(R.color.coral));
        }
    }

    //
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            AddDots(position);

            //Initialising current slide position variable
            currentSlide = position;


            if(position == 0){

                //Hiding Get Started Button
                get_started_btn.setVisibility(View.INVISIBLE);

            }
            else if(position == 1){

                //Hiding Get Started Button
                get_started_btn.setVisibility(View.INVISIBLE);

            }
            else if(position == 2){

                //Hiding Get Started Button
                get_started_btn.setVisibility(View.INVISIBLE);
            }
            else if(position == 3){

                //Hiding Get Started Button
                get_started_btn.setVisibility(View.INVISIBLE);
            }
            else{

                //Setting Button Animation For When It Is Visible
                animation = AnimationUtils.loadAnimation(Onboard.this,R.anim.bottom_animation);
                get_started_btn.setAnimation(animation);

                //Showing Get Started Get Started On Last Slide
                get_started_btn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}