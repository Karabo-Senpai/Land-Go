package com.example.opsc7312_task2;

public class FavouriteLandmarks {


    String preferred_landmark;

    //Default Constructor
    public FavouriteLandmarks() {
    }

    //Non Default Constructor
    public FavouriteLandmarks(String preferred_landmark) {
        this.preferred_landmark = preferred_landmark;
    }

    public String getPreferred_landmark() {
        return preferred_landmark;
    }

    public void setPreferred_landmark(String preferred_landmark) {
        this.preferred_landmark = preferred_landmark;
    }

    @Override
    public String toString()
    {
        return "FavouriteLandmarks{" +
                "Preferred Landmark = '" + preferred_landmark + "' +'}";
    }
}
