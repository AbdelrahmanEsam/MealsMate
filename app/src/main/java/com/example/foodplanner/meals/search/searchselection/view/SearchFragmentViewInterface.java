package com.example.foodplanner.meals.search.searchselection.view;

import java.util.List;

public interface SearchFragmentViewInterface {


    void onGetAllCategoriesCallback(List<String> categories);
    void onGetAllIngredientsCallback(List<String> ingredients);
    void onGetAllCountriesCallback(List<String> countries);

    void onGetAllCategoriesFailureCallback(String error);
    void onGetAllIngredientsFailureCallback(String error);
    void onGetAllCountriesFailureCallback(String error);
}
