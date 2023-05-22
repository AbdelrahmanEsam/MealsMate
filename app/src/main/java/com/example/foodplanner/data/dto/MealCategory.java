package com.example.foodplanner.data.dto;

import com.google.gson.annotations.SerializedName;

public class MealCategory {
    @SerializedName("strCategory")
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
