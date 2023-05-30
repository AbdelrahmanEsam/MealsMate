package com.example.foodplanner.meals.search.searchresults.view;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.search.FilterMeal;

import java.util.List;

public interface SearchResultsViewInterface {

    public  void onResultsSuccessCallback(List<FilterMeal> meals);
    public  void onResultsFailureCallback(String error);
    public  void onGetDetailsSuccessCallback(Meal meal,String requester);
    public  void onGetDetailsFailureCallback(String error);
}
