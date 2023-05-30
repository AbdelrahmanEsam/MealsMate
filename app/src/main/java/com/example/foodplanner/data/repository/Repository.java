package com.example.foodplanner.data.repository;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.meal.MealsResponse;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;
import com.example.foodplanner.data.local.LocalDataSource;
import com.example.foodplanner.meals.mainmealsfragment.view.MealOfTheDayCallback;
import com.example.foodplanner.data.remote.RemoteDataSource;
import com.example.foodplanner.meals.search.searchresults.presenter.SearchResultsPresenterInterface;
import com.example.foodplanner.meals.search.searchselection.presenter.SearchSelectionPresenterInterface;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class Repository implements RepositoryInterface{

    private final RemoteDataSource remote;
    private final LocalDataSource local;
    private static Repository instance ;

    private Repository(RemoteDataSource remote, LocalDataSource local)
    {

        this.remote = remote;
        this.local = local;

    }


    public  static synchronized Repository getInstance(RemoteDataSource remote,LocalDataSource local)
    {

        if (instance == null)
        {

            instance = new Repository(remote,local);
        }

        return instance;
    }




    @Override
    public void getMealOfTheDay(MealOfTheDayCallback mealOfTheDayCallback) {
        remote.getMealOfTheDay(mealOfTheDayCallback);
    }

    @Override
    public void filterMealsByCountry(String country, SearchResultsPresenterInterface presenterInterface) {

        remote.filterMealsByCountry(country,presenterInterface);
    }

    @Override
    public void filterMealsByIngredient(String ingredients, SearchResultsPresenterInterface presenterInterface) {

        remote.filterMealsByIngredient(ingredients,presenterInterface);
    }

    @Override
    public void filterMealsByCategory(String category, SearchResultsPresenterInterface presenterInterface) {

        remote.filterMealsByCategory(category,presenterInterface);
    }

    @Override
    public void getFullDetailsById(String id,String requester, SearchResultsPresenterInterface presenterInterface) {

        remote.getFullDetailsById(id,requester,presenterInterface);
    }

    @Override
    public Observable<MealsResponse> searchByNameMealRequest(String prefix) {
        return remote.searchByNameMealRequest(prefix);
    }




    @Override
    public void getAllCategories(SearchSelectionPresenterInterface presenterInterface) {
       remote.getAllCategories(presenterInterface);
    }

    @Override
    public void getAllCountries(SearchSelectionPresenterInterface presenterInterface) {
        remote.getAllCountries(presenterInterface);
    }

    @Override
    public void getAllIngredients(SearchSelectionPresenterInterface presenterInterface) {
        remote.getAllIngredients(presenterInterface);
    }

    @Override
    public Completable insertMealToBreakfast(Meal meal,String day) {
        return local.insertMealToBreakfast(meal,day);
    }

    @Override
    public Completable insertMealToLaunch(Meal meal,String day) {

        return  local.insertMealToLaunch(meal,day);
    }

    @Override
    public Completable insertMealToDinner(Meal meal,String day) {

        return local.insertMealToDinner(meal,day);
    }

    @Override
    public Completable insertMealToFavourite(Meal meal,String day) {

        return local.insertMealToFavourite(meal,day);
    }

    @Override
    public Completable deleteMealFromBreakfast(Meal meal) {

       return local.deleteMealFromBreakfast(meal);
    }

    @Override
    public Completable deleteMealFromLaunch(Meal meal) {

        return   local.deleteMealFromLaunch(meal);
    }

    @Override
    public Completable deleteMealFromDinner(Meal meal) {

        return local.deleteMealFromDinner(meal);
    }

    @Override
    public Completable deleteMealFromFavourite(Meal meal) {

        return local.deleteMealFromFavourite(meal);
    }

    @Override
    public Observable<List<Favourite>> getAllFavouriteMeals() {
        return local.getAllFavouriteMeals();
    }

    @Override
    public Observable<List<Breakfast>> getAllBreakfastMeals() {
        return local.getAllBreakfastMeals();
    }

    @Override
    public Observable<List<Launch>> getAllLaunchMeals() {
        return local.getAllLaunchMeals();
    }

    @Override
    public Observable<List<Dinner>> getAllDinnerMeals() {
        return local.getAllDinnerMeals();
    }

    @Override
    public void clearAllTables() {
        local.clearAllTables();
    }
}
