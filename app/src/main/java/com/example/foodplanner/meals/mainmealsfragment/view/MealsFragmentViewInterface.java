package com.example.foodplanner.meals.mainmealsfragment.view;

import com.example.foodplanner.data.dto.Meal;

import java.util.List;

public interface MealsFragmentViewInterface {
    public void onResultSuccessAllMealsCallback(List<Meal> meals);
    public void onResultFailureAllMealsCallback(String error);
    public void onResultSuccessOneMealsCallback(Meal meal);
    public void onResultFailureOneMealCallback(String error);
}
