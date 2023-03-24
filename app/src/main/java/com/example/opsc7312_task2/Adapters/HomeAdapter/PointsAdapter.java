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

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.FeaturedViewHolder>{


    ArrayList<PointsHelper> points_categories;


    public PointsAdapter(ArrayList<PointsHelper> pointsHelpers) {
        this.points_categories = pointsHelpers;
    }

    @NonNull
    @Override
    public PointsAdapter.FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.points_of_interest,parent,false);

        //Passing View To View Holder
        PointsAdapter.FeaturedViewHolder featuredViewHolder = new PointsAdapter.FeaturedViewHolder(view);

        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        PointsHelper pointsHelper = points_categories.get(position);

        holder.image.setImageResource(pointsHelper.getImage());
        holder.point_of_interest_name.setText(pointsHelper.getPoint_name());
        holder.point_of_interest_address.setText(pointsHelper.getPoint_desc());
    }



    @Override
    public int getItemCount() {
        return points_categories.size();
    }


    //
    public static class FeaturedViewHolder extends  RecyclerView.ViewHolder{

        //Declaring Hooks For XML Components
        ImageView image;
        TextView point_of_interest_name,point_of_interest_address;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            //Assigning Hooks To Respective XML Components
            image = itemView.findViewById(R.id.points_of_interest_image);
            point_of_interest_name = itemView.findViewById(R.id.point_name);
            point_of_interest_address = itemView.findViewById(R.id.point_address);
        }
    }



}
