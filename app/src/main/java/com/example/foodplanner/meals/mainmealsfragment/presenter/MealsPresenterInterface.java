package com.example.foodplanner.meals.mainmealsfragment.presenter;

import com.example.foodplanner.data.dto.Meal;

public interface MealsPresenterInterface {

    public void getAllMeals();
    public void mealOfTheDayRequest();
    public void insertMealToBreakfast(Meal meal);
    public  void insertMealToLaunch(Meal meal);
    public  void insertMealToDinner(Meal meal);
    public void insertMealToFavourite(Meal meal);

}
