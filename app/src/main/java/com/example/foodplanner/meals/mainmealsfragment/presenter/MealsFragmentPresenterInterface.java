package com.example.foodplanner.meals.mainmealsfragment.presenter;

import com.example.foodplanner.data.dto.Meal;

public interface MealsFragmentPresenterInterface {

    public void getAllMeals();
    public void getMealOfTheDay();
    public void insertMealToBreakfast(Meal meal);
    public  void insertMealToLaunch(Meal meal);
    public  void insertMealToDinner(Meal meal);
    public void insertMealToFavourite(Meal meal);

}
