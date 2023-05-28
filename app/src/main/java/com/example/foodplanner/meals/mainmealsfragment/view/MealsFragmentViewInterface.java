package com.example.foodplanner.meals.mainmealsfragment.view;

import android.widget.ImageView;

import com.example.foodplanner.data.dto.Meal;

import java.util.List;

public interface MealsFragmentViewInterface {
    public void onResultSuccessAllMealsCallback();
    public void onResultFailureAllMealsCallback(String error);
    public void onResultSuccessOneMealsCallback();
    public void onResultFailureOneMealCallback(String error);

    void onMealAddClicked(Meal meal);

    public  void onMealClicked(Meal meal, ImageView transitionView);
}
