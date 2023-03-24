package com.example.opsc7312_task2.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.opsc7312_task2.R;

//Adapter Class To Show Slides Images , Titles And Descriptions
public class SliderAdapter extends PagerAdapter {

    //Creating Context
    Context context;

    //Creating Layout Inflater
    LayoutInflater inflater;
    //Creating Constructor For Slider Context Should The User Wish To Move To A Different Slide
    public SliderAdapter(Context context) {
        this.context = context;
    }

    //Creating Arrays For Slider Image Components
    int images[] = {

            R.drawable.travel,
            R.drawable.viewmap,
            R.drawable.searchmap2,
            R.drawable.navigate2,
            R.drawable.save

    };

    //Creating Arrays For Slider Image Components
    int headings[] = {

            R.string.first_slide,
            R.string.second_slide,
            R.string.third_slide,
            R.string.forth_slide,
            R.string.fifth_slide

    };

    //Creating Arrays For Slider Image Components
    int descriptions[] = {

            R.string.first_slide_desc,
            R.string.second_slide_desc,
            R.string.third_slide_desc,
            R.string.forth_slide_desc,
            R.string.fifth_slide_desc
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

       inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);


       View view = inflater.inflate(R.layout.slides_layout,container,false);

       //Getting Images, Heading And Descriptions
        ImageView imageView = view.findViewById(R.id.slide_image);
        TextView heading = view.findViewById(R.id.slide_headings);
        TextView desc = view.findViewById(R.id.slide_descriptions);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(descriptions[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
