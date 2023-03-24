package com.example.opsc7312_task2;

public class PreferredLandmarkType {

    String landmark_name,description;
    Double latitude;
    Double longitude;

    public PreferredLandmarkType() {
    }

    public PreferredLandmarkType(String landmark_name, String description, Double latitude, Double longitude) {
        this.landmark_name = landmark_name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PreferredLandmarkType(String selected_preferred_type) {
    }

    public String getLandmark_name() {
        return landmark_name;
    }

    public void setLandmark_name(String landmark_name) {
        this.landmark_name = landmark_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString()
    {
        return "UserFavorites{" +
                "Location  ='" + landmark_name + '\'' +
                ", Latitude =" + latitude +
                ", Longitude =" + longitude +
                ", Description =" + description +
                '}';
    }
}
