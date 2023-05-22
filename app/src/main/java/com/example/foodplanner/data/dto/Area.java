package com.example.foodplanner.data.dto;

import com.google.gson.annotations.SerializedName;

public class Area {
    @SerializedName("strArea")
    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
