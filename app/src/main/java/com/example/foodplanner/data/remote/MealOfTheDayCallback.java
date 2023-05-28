package com.example.foodplanner.data.remote;

import com.example.foodplanner.data.dto.Meal;

public interface MealOfTheDayCallback {

    public void onResultSuccessMealOfTheDayCallback(Meal meal);
    public void onResultFailureMealOfTheDayCallback(String error);
}
