package com.example.foodplanner.data.remote;


import com.example.foodplanner.data.dto.category.AllCategoriesResponse;
import com.example.foodplanner.data.dto.area.AreaResponse;
import com.example.foodplanner.data.dto.ingredients.IngredientsResponse;
import com.example.foodplanner.data.dto.meal.MealsResponse;
import com.example.foodplanner.data.dto.search.FilterMealResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiProvider {

    @GET("random.php")
    Call<MealsResponse> getMealOfTheDay();

    @GET("search.php")
    Observable<MealsResponse> searchMealsByFirstLetter(@Query("f") String letter);

    @GET("search.php")
    Observable<MealsResponse> searchMealsByName(@Query("s") String name);

    @GET("filter.php")
    Call<FilterMealResponse> filterMealsByIngredient(@Query("i") String IngredientName);

    @GET("filter.php")
    Call<FilterMealResponse> filterMealsByCategory(@Query("c") String categoryName);

    @GET("filter.php")
    Call<FilterMealResponse> filterMealsByCountry(@Query("a") String countryName);

    @GET("lookup.php")
    Call<MealsResponse> getFullDetailsById(@Query("i") String id);



    @GET("list.php?c=list")
    Call<AllCategoriesResponse> getAllCategories();


    @GET("list.php?a=list")
    Call<AreaResponse> getAllCountries();


    @GET("list.php?i=list")
    Call<IngredientsResponse> getAllIngredients();

}

