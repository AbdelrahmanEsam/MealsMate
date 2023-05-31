package com.example.foodplanner.data.remote;

import com.example.foodplanner.data.dto.area.AreaResponse;
import com.example.foodplanner.data.dto.category.AllCategoriesResponse;
import com.example.foodplanner.data.dto.ingredients.IngredientsResponse;
import com.example.foodplanner.data.dto.meal.MealsResponse;
import com.example.foodplanner.data.dto.search.FilterMealResponse;
import com.example.foodplanner.meals.mainmealsfragment.view.MealOfTheDayCallback;
import com.example.foodplanner.meals.search.searchresults.presenter.SearchResultsPresenterInterface;
import com.example.foodplanner.meals.search.searchselection.presenter.SearchSelectionPresenterInterface;

import io.reactivex.rxjava3.core.Observable;

public interface RemoteDataSource {




    public  Observable<MealsResponse>  getMealOfTheDay();

    public Observable<FilterMealResponse> filterMealsByCountry(String country);

    public Observable<FilterMealResponse> filterMealsByIngredient(String ingredients);

    public  Observable<FilterMealResponse>  filterMealsByCategory(String category);

    public  Observable<MealsResponse> getFullDetailsById(String id,String requester);
    public Observable<MealsResponse> searchByNameMealRequest(String prefix);


    public Observable<AllCategoriesResponse> getAllCategories();


    public Observable<AreaResponse> getAllCountries();

    public Observable<IngredientsResponse> getAllIngredients();

}
