package com.example.foodplanner.schedule.view;

import android.view.View;

import com.example.foodplanner.data.dto.Meal;

public interface OnMealLongClickedListener {

    void onMealLongClicked(View view , Meal meal , int mealType);
}
