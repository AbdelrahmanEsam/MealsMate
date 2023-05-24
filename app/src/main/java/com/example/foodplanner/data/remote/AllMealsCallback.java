package com.example.foodplanner.data.remote;

import com.example.foodplanner.data.dto.Meal;

import java.util.List;

public interface AllMealsCallback {
    public void onResultSuccessAllMealsCallback(List<Meal> meals);
    public void onResultFailureAllMealsCallback(String error);
}
