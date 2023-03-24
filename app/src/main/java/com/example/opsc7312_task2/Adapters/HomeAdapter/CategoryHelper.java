package com.example.opsc7312_task2.Adapters.HomeAdapter;

public class CategoryHelper {

    int image;
    String category_name;

    public CategoryHelper(int image, String category_name) {
        this.image = image;
        this.category_name = category_name;
    }

    public int getImage() {
        return image;
    }

    public String getCategory_name() {
        return category_name;
    }
}
