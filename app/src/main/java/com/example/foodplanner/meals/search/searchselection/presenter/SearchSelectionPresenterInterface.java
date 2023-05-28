package com.example.foodplanner.meals.search.searchselection.presenter;

import com.example.foodplanner.data.dto.area.Area;
import com.example.foodplanner.data.dto.category.MealCategory;
import com.example.foodplanner.data.dto.ingredients.Ingredient;

import java.util.List;

public interface SearchSelectionPresenterInterface {


    void getAllCategories();

    void getAllIngredients();

    void getAllCountries();

    void onGetAllCategoriesSuccessCallback(List<MealCategory> categories);
    void onGetAllIngredientsSuccessCallback(List<Ingredient> ingredients);
    void onGetAllCountriesSuccessCallback(List<Area> countries);


    void onGetAllCategoriesFailureCallback(String error);
    void onGetAllIngredientsFailureCallback(String error);
    void onGetAllCountriesFailureCallback(String error);

    
}
