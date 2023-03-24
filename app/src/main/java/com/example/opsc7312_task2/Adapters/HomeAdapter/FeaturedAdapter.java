package com.example.opsc7312_task2.Adapters.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opsc7312_task2.R;

import java.util.ArrayList;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {


    ArrayList<FeaturedHelper> featuredLocations;


    public FeaturedAdapter(ArrayList<FeaturedHelper> featuredLocations) {
        this.featuredLocations = featuredLocations;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_land_design,parent,false);

        //Passing View To View Holder
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);

        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {


        FeaturedHelper featuredHelper = featuredLocations.get(position);

        holder.image.setImageResource(featuredHelper.getImage());
        holder.landmark_name.setText(featuredHelper.getLandmark_name());
        holder.landmark_location.setText(featuredHelper.getLandmark_address());



    }

    @Override
    public int getItemCount() {
        return featuredLocations.size();
    }


    //
    public static class FeaturedViewHolder extends  RecyclerView.ViewHolder{

        //Declaring Hooks For XML Components
        ImageView image;
        TextView landmark_name,landmark_location;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            //Assigning Hooks To Respective XML Components
            image = itemView.findViewById(R.id.featured_image);
            landmark_name = itemView.findViewById(R.id.featured_landmark_name);
            landmark_location = itemView.findViewById(R.id.featured_landmark_address);
        }
    }
}
