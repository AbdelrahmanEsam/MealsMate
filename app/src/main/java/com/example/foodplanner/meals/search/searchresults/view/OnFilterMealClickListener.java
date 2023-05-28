package com.example.foodplanner.meals.search.searchresults.view;

import android.widget.ImageView;

import com.example.foodplanner.data.dto.search.FilterMeal;

import java.util.logging.Filter;

public interface OnFilterMealClickListener {

    void onFilterMealClickListener(FilterMeal meal , ImageView transitionView);

}
