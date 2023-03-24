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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.FeaturedViewHolder>{

    ArrayList<CategoryHelper> landmark_categories;

    public CategoryAdapter(ArrayList<CategoryHelper> categoryHelpers) {
        this.landmark_categories = categoryHelpers;
    }

    @NonNull
    @Override
    public CategoryAdapter.FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_card_design,parent,false);

        //Passing View To View Holder
        CategoryAdapter.FeaturedViewHolder featuredViewHolder = new CategoryAdapter.FeaturedViewHolder(view);

        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.FeaturedViewHolder holder, int position) {
        CategoryHelper categoryHelper = landmark_categories.get(position);

        holder.image.setImageResource(categoryHelper.getImage());
        holder.category_name.setText(categoryHelper.getCategory_name());

    }



    @Override
    public int getItemCount() {
        return landmark_categories.size();
    }


    //
    public static class FeaturedViewHolder extends  RecyclerView.ViewHolder{

        //Declaring Hooks For XML Components
        ImageView image;
        TextView category_name;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            //Assigning Hooks To Respective XML Components
            image = itemView.findViewById(R.id.category_image);
            category_name = itemView.findViewById(R.id.category_name);

        }
    }

}
