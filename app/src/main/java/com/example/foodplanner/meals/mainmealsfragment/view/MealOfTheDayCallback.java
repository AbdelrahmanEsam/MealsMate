package com.example.foodplanner.meals.mainmealsfragment.view;

import com.example.foodplanner.data.dto.Meal;

public interface MealOfTheDayCallback {

    public void onResultSuccessMealOfTheDayCallback(Meal meal);
    public void onResultFailureMealOfTheDayCallback(String error);
}
