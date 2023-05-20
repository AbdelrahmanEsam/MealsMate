package com.example.foodplanner.data;


import com.example.foodplanner.data.dto.MealsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


interface ApiProvider {

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
}
