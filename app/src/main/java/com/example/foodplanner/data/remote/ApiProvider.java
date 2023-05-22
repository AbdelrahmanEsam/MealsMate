package com.example.foodplanner.data.remote;


import com.example.foodplanner.data.dto.AllCategoriesResponse;
import com.example.foodplanner.data.dto.AreaResponse;
import com.example.foodplanner.data.dto.IngredientsResponse;
import com.example.foodplanner.data.dto.MealsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiProvider {

    @GET("random.php")
    Call<MealsResponse> getMealOfTheDay();

    @GET("search.php")
    Call<MealsResponse> searchMealsByFirstLetter(@Query("f") String letter);

    @GET("search.php")
    Call<MealsResponse> searchMealsByName(@Query("s") String name);

    @GET("filter.php")
    Call<MealsResponse> filterMealsByIngredient(@Query("i") String IngredientName);

    @GET("filter.php")
    Call<MealsResponse> filterMealsByCategory(@Query("c") String categoryName);

    @GET("filter.php")
    Call<MealsResponse> filterMealsByCountry(@Query("a") String countryName);



    @GET("list.php?c=list")
    Call<AllCategoriesResponse> getAllCategories();


    @GET("list.php?a=list")
    Call<AreaResponse> getAllCountries();


    @GET("list.php?i=list")
    Call<IngredientsResponse> getAllIngredients();

}

