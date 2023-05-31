package com.example.foodplanner.data.repository;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.area.AreaResponse;
import com.example.foodplanner.data.dto.category.AllCategoriesResponse;
import com.example.foodplanner.data.dto.ingredients.IngredientsResponse;
import com.example.foodplanner.data.dto.meal.MealsResponse;
import com.example.foodplanner.data.dto.search.FilterMealResponse;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;
import com.example.foodplanner.meals.mainmealsfragment.view.MealOfTheDayCallback;
import com.example.foodplanner.meals.search.searchresults.presenter.SearchResultsPresenterInterface;
import com.example.foodplanner.meals.search.searchselection.presenter.SearchSelectionPresenterInterface;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface RepositoryInterface {


    public Observable<MealsResponse>  getMealOfTheDay();

    public Observable<FilterMealResponse> filterMealsByCountry(String country);

    public Observable<FilterMealResponse> filterMealsByIngredient(String ingredients);

    public Observable<FilterMealResponse>  filterMealsByCategory(String category );


    public Observable<MealsResponse> getFullDetailsById(String id,String requester);


    public Observable<MealsResponse> searchByNameMealRequest(String prefix);



    public Observable<AllCategoriesResponse> getAllCategories();


    public Observable<AreaResponse> getAllCountries();

    public Observable<IngredientsResponse> getAllIngredients();



    public Completable insertMealToBreakfast(Meal meal);

    public Completable insertMealToLaunch(Meal meal);

    public Completable insertMealToDinner(Meal meal);

    public Completable insertMealToFavourite(Meal meal);


    public Completable deleteMealFromBreakfast(Meal meal);
    public Completable deleteMealFromLaunch(Meal meal);
    public Completable deleteMealFromDinner(Meal meal);
    public Completable deleteMealFromFavourite(Meal meal);

    public Observable<List<Favourite>> getAllFavouriteMeals();
    public Observable<List<Breakfast>> getAllBreakfastMeals();
    public Observable<List<Launch>> getAllLaunchMeals();
    public Observable<List<Dinner>> getAllDinnerMeals();

    public void clearAllTables();

}
