package com.example.foodplanner.data.remote;

import com.example.foodplanner.data.dto.AllCategoriesResponse;
import com.example.foodplanner.data.dto.AreaResponse;
import com.example.foodplanner.data.dto.IngredientsResponse;
import com.example.foodplanner.data.dto.MealsResponse;

public interface RemoteDataSource {


    public void getAllMealsResponse(AllMealsCallback networkCallback);

    public  void getMealOfTheDay(MealOfTheDayCallback mealOfTheDayCallback);

    public MealsResponse filterMealsByCountry();

    public MealsResponse filterMealsByIngredient();

    public MealsResponse filterMealsByCategory();

    public MealsResponse searchMealsByName();


    public AllCategoriesResponse getAllCategories();


    public AreaResponse getAllCountries();

    public IngredientsResponse getAllIngredients();

}
