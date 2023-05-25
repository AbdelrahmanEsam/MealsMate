package com.example.foodplanner.meals.mainmealsfragment.view;

import android.widget.ImageView;

import com.example.foodplanner.data.dto.Meal;

public interface OnMealClickListener {

    public  void onMealClicked(Meal meal, ImageView transitionView);
}
