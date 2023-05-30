package com.example.foodplanner.meals.mainmealsfragment.presenter;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.meal.MealsResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface MealsPresenterInterface {


    public void mealOfTheDayRequest();

    public void searchByNameMealRequest(String prefix);

    public Completable insertMealToBreakfast(Meal meal);
    public  Completable insertMealToLaunch(Meal meal);
    public  Completable insertMealToDinner(Meal meal);
    public Completable insertMealToFavourite(Meal meal);

}
