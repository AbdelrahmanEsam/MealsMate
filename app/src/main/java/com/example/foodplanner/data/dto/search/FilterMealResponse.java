
package com.example.foodplanner.data.dto.search;

import java.util.List;
import com.google.gson.annotations.SerializedName;



public class FilterMealResponse {

    @SerializedName("meals")
    private List<FilterMeal> mMeals;

    public List<FilterMeal> getMeals() {
        return mMeals;
    }

    public void setMeals(List<FilterMeal> meals) {
        mMeals = meals;
    }

}
