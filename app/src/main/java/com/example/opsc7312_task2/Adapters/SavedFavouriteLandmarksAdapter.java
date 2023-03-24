package com.example.opsc7312_task2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opsc7312_task2.PreferredLandmarkType;
import com.example.opsc7312_task2.R;

import java.util.ArrayList;

public class SavedFavouriteLandmarksAdapter extends RecyclerView.Adapter<SavedFavouriteLandmarksAdapter.ItemsViewHolder> {


   //Declaring Arraylist And Context Variables
    ArrayList<PreferredLandmarkType> landmarkTypes;
    Context context;

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_favorite_landmarks,parent,false);
        ItemsViewHolder itemViewHolder = new ItemsViewHolder(view);

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {

        //Binding Items To View
        PreferredLandmarkType currently_saved_landmark = landmarkTypes.get(position);
        holder.landmark_name.setText(currently_saved_landmark.getLandmark_name());
        holder.landmark_desc.setText(currently_saved_landmark.getDescription());
        holder.latitude.setText(currently_saved_landmark.getLatitude().toString());
        holder.longitude.setText(currently_saved_landmark.getLongitude().toString());
    }

    //
    public class ItemsViewHolder extends RecyclerView.ViewHolder {

        public TextView landmark_name, landmark_desc,latitude, longitude;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            landmark_name = itemView.findViewById(R.id.land_name);
            landmark_desc = itemView.findViewById(R.id.land_desc);
            latitude = itemView.findViewById(R.id.lati);
            longitude = itemView.findViewById(R.id.longi);
        }
    }

    @Override
    public int getItemCount() {
        return landmarkTypes.size();
    }

    public SavedFavouriteLandmarksAdapter(ArrayList<PreferredLandmarkType> saved_list,Context context){

        landmarkTypes = saved_list;
        this.context = context;
    }

}
