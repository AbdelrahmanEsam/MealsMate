package com.example.foodplanner.meals.mainmealsfragment.view;

import android.view.View;
import android.widget.ImageView;

import com.example.foodplanner.data.dto.Meal;

public interface OnMealClickListener {

    public  void onMealClickListener(Meal meal, ImageView transitionView);
}
