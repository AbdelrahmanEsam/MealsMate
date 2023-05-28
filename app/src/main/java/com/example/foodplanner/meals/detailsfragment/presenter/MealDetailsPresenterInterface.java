package com.example.foodplanner.meals.detailsfragment.presenter;

import com.example.foodplanner.data.dto.Meal;

public interface MealDetailsPresenterInterface {

    public void insertMealToBreakfast(Meal meal);
    public  void insertMealToLaunch(Meal meal);
    public  void insertMealToDinner(Meal meal);
    public void insertMealToFavourite(Meal meal);

    public Meal getMealToAdd();

    public void setMealToAdd(Meal meal);

}
