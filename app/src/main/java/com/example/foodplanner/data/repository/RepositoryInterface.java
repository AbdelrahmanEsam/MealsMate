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
import com.example.foodplanner.data.remote.AllMealsCallback;
import com.example.foodplanner.data.remote.MealOfTheDayCallback;

import java.util.List;

public interface RepositoryInterface {

    public void getAllMealsResponse(AllMealsCallback networkCallback);

    public void getMealOfTheDay(MealOfTheDayCallback mealOfTheDayCallback);

    public MealsResponse filterMealsByCountry();

    public MealsResponse filterMealsByIngredient();

    public MealsResponse filterMealsByCategory();

    public MealsResponse searchMealsByName();


    public AllCategoriesResponse getAllCategories();


    public AreaResponse getAllCountries();

    public IngredientsResponse getAllIngredients();



    public void insertMealToBreakfast(Meal meal,String day);

    public void insertMealToLaunch(Meal meal,String day);

    public void insertMealToDinner(Meal meal,String day);

    public void insertMealToFavourite(Meal meal,String day);


    public void deleteMealFromBreakfast(Meal meal);
    public void deleteMealFromLaunch(Meal meal);
    public void deleteMealFromDinner(Meal meal);
    public void deleteMealFromFavourite(Meal meal);

    public LiveData<List<Favourite>> getAllFavouriteMeals();
    public LiveData<List<Breakfast>> getAllBreakfastMeals();
    public LiveData<List<Launch>> getAllLaunchMeals();
    public LiveData<List<Dinner>> getAllDinnerMeals();

    public void clearAllTables();

}
