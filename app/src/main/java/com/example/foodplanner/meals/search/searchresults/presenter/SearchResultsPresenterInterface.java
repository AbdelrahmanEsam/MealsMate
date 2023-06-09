package com.example.foodplanner.meals.search.searchresults.presenter;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.ingredients.Ingredient;
import com.example.foodplanner.data.dto.meal.MealsResponse;
import com.example.foodplanner.data.dto.search.FilterMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public interface SearchResultsPresenterInterface {

    void filterMealsByCategory(String category);

    void filterMealsByIngredient(String ingredient);

    void filterMealsByCountry(String country);

    void getFullDetailsMealRequest(String id,String requester);



    public Completable insertMealToBreakfast(Meal meal);
    public  Completable insertMealToLaunch(Meal meal);
    public  Completable insertMealToDinner(Meal meal);
    public Completable insertMealToFavourite(Meal meal);

}
