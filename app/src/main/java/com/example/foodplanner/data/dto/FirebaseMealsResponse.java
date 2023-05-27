package com.example.foodplanner.data.dto;

import java.util.List;

public class FirebaseMealsResponse {
    public List<Meal> meals;

    public FirebaseMealsResponse()
    {

    }
    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
