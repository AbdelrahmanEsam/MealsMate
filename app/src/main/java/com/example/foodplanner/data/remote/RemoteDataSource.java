package com.example.foodplanner.data.remote;

import com.example.foodplanner.data.dto.meal.MealsResponse;
import com.example.foodplanner.meals.search.searchresults.presenter.SearchResultsPresenterInterface;
import com.example.foodplanner.meals.search.searchselection.presenter.SearchSelectionPresenterInterface;

public interface RemoteDataSource {


    public void getAllMealsResponse(AllMealsCallback networkCallback);

    public  void getMealOfTheDay(MealOfTheDayCallback mealOfTheDayCallback);

    public void filterMealsByCountry(String country, SearchResultsPresenterInterface presenterInterface);

    public void filterMealsByIngredient(String ingredients, SearchResultsPresenterInterface presenterInterface);

    public void  filterMealsByCategory(String category, SearchResultsPresenterInterface presenterInterface);

    public void getFullDetailsById(String id, SearchResultsPresenterInterface presenterInterface);
    public MealsResponse searchMealsByName();


    public void getAllCategories(SearchSelectionPresenterInterface presenterInterface);


    public void getAllCountries(SearchSelectionPresenterInterface presenterInterface);

    public void getAllIngredients(SearchSelectionPresenterInterface presenterInterface);

}
