package com.example.foodplanner.meals.search.searchselection.presenter;

import com.example.foodplanner.data.dto.Area;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.MealCategory;

import java.util.List;

public interface SearchSelectionPresenterInterface {


    void getAllCategories();

    void getAllIngredients();

    void getAllCountries();

    void onGetAllCategoriesCallback(List<MealCategory> categories);
    void onGetAllIngredientsCallback(List<Meal> ingredients);
    void onGetAllCountriesCallback(List<Area> countries);

    
}
