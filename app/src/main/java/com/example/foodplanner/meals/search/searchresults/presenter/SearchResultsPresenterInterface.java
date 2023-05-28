package com.example.foodplanner.meals.search.searchresults.presenter;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.ingredients.Ingredient;
import com.example.foodplanner.data.dto.meal.MealsResponse;
import com.example.foodplanner.data.dto.search.FilterMeal;

import java.util.List;

public interface SearchResultsPresenterInterface {

    void filterMealsByCategory(String category);

    void filterMealsByIngredient(String ingredient);

    void filterMealsByCountry(String country);

    void getFullDetailsMealRequest(String id);


    void onFilterCategorySuccessCallback(List<FilterMeal> categories);
    void onFilterIngredientSuccessCallback(List<FilterMeal> ingredients);
    void onFilterCountrySuccessCallback(List<FilterMeal> countries);
    void onGetItemFullDetailsSuccessCallback(List<Meal> meals);



    void onFilterCategoryFailureCallback(String error);
    void onFilterIngredientFailureCallback(String error);
    void onFilterCountryFailureCallback(String error);

}
