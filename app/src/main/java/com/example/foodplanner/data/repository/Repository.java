package com.example.foodplanner.data.repository;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.data.dto.AllCategoriesResponse;
import com.example.foodplanner.data.dto.AreaResponse;
import com.example.foodplanner.data.dto.IngredientsResponse;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.MealsResponse;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;
import com.example.foodplanner.data.local.LocalDataSource;
import com.example.foodplanner.data.remote.AllMealsCallback;
import com.example.foodplanner.data.remote.MealOfTheDayCallback;
import com.example.foodplanner.data.remote.RemoteDataSource;

import java.util.List;

public class Repository implements RepositoryInterface{

    private RemoteDataSource remote;
    private LocalDataSource local;
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
    public void getAllMealsResponse(AllMealsCallback networkCallback) {
        remote.getAllMealsResponse(networkCallback);
    }

    @Override
    public void getMealOfTheDay(MealOfTheDayCallback mealOfTheDayCallback) {
        remote.getMealOfTheDay(mealOfTheDayCallback);
    }

    @Override
    public MealsResponse filterMealsByCountry() {
        return null;
    }

    @Override
    public MealsResponse filterMealsByIngredient() {
        return null;
    }

    @Override
    public MealsResponse filterMealsByCategory() {
        return null;
    }

    @Override
    public MealsResponse searchMealsByName() {
        return null;
    }

    @Override
    public AllCategoriesResponse getAllCategories() {
        return null;
    }

    @Override
    public AreaResponse getAllCountries() {
        return null;
    }

    @Override
    public IngredientsResponse getAllIngredients() {
        return null;
    }

    @Override
    public void insertMealToBreakfast(Meal meal,String day) {
        local.insertMealToBreakfast(meal,day);
    }

    @Override
    public void insertMealToLaunch(Meal meal,String day) {

        local.insertMealToLaunch(meal,day);
    }

    @Override
    public void insertMealToDinner(Meal meal,String day) {

        local.insertMealToDinner(meal,day);
    }

    @Override
    public void insertMealToFavourite(Meal meal,String day) {

        local.insertMealToFavourite(meal,day);
    }

    @Override
    public void deleteMealFromBreakfast(Meal meal) {

        local.deleteMealFromBreakfast(meal);
    }

    @Override
    public void deleteMealFromLaunch(Meal meal) {

        local.deleteMealFromLaunch(meal);
    }

    @Override
    public void deleteMealFromDinner(Meal meal) {

        local.deleteMealFromDinner(meal);
    }

    @Override
    public void deleteMealFromFavourite(Meal meal) {

        local.deleteMealFromFavourite(meal);
    }

    @Override
    public LiveData<List<Favourite>> getAllFavouriteMeals() {
        return local.getAllFavouriteMeals();
    }

    @Override
    public LiveData<List<Breakfast>> getAllBreakfastMeals() {
        return local.getAllBreakfastMeals();
    }

    @Override
    public LiveData<List<Launch>> getAllLaunchMeals() {
        return local.getAllLaunchMeals();
    }

    @Override
    public LiveData<List<Dinner>> getAllDinnerMeals() {
        return local.getAllDinnerMeals();
    }

    @Override
    public void clearAllTables() {
        local.clearAllTables();
    }
}
