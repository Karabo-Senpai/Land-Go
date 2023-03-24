package com.example.opsc7312_task2.Adapters.HomeAdapter;

public class FeaturedHelper {

    int image;
    String landmark_name, landmark_address;

    //Non Default Constructor
    public FeaturedHelper(int image, String landmark_name, String landmark_address) {
        this.image = image;
        this.landmark_name = landmark_name;
        this.landmark_address = landmark_address;
    }

    //Creating Getters
    public int getImage() {
        return image;
    }

    public String getLandmark_name() {
        return landmark_name;
    }
    public String getLandmark_address() {
        return landmark_address;
    }
}
