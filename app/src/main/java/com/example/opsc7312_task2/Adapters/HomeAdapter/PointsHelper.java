package com.example.opsc7312_task2.Adapters.HomeAdapter;

public class PointsHelper {

    int image;
    String point_name, point_desc;

    public PointsHelper(int image, String point_name, String point_desc) {
        this.image = image;
        this.point_name = point_name;
        this.point_desc = point_desc;
    }

    public int getImage() {
        return image;
    }

    public String getPoint_name() {
        return point_name;
    }

    public String getPoint_desc() {
        return point_desc;
    }
}
