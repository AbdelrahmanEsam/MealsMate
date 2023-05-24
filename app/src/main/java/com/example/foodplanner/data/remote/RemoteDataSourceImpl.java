package com.example.foodplanner.data.remote;

import com.example.foodplanner.data.dto.AllCategoriesResponse;
import com.example.foodplanner.data.dto.AreaResponse;
import com.example.foodplanner.data.dto.IngredientsResponse;
import com.example.foodplanner.data.dto.MealsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSourceImpl implements RemoteDataSource{


    private static ApiProvider retrofit = null;
    private static RemoteDataSourceImpl remoteDataSource;

    private RemoteDataSourceImpl()
    {

        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiProvider.class);
        }
    }

    public static synchronized  RemoteDataSourceImpl getInstance() {

        if (remoteDataSource == null)
        {
            remoteDataSource = new RemoteDataSourceImpl();
        }
      return remoteDataSource;
    }





    @Override
    public void getAllMealsResponse(AllMealsCallback networkCallback) {
        retrofit.searchMealsByName("").enqueue(new Callback<MealsResponse>() {
            @Override
            public void onResponse(Call<MealsResponse> call, Response<MealsResponse> response) {

               networkCallback.onResultSuccessAllMealsCallback(response.body().getMeals());
            }

            @Override
            public void onFailure(Call<MealsResponse> call, Throwable t) {
              networkCallback.onResultFailureAllMealsCallback(t.getMessage());
            }
        });
    }

    @Override
    public void getMealOfTheDay(MealOfTheDayCallback mealOfTheDayCallback) {

        retrofit.getMealOfTheDay().enqueue(new Callback<MealsResponse>() {
            @Override
            public void onResponse(Call<MealsResponse> call, Response<MealsResponse> response) {
                mealOfTheDayCallback.onResultSuccessMealOfTheDayCallback(response.body().getMeals().get(0));
            }

            @Override
            public void onFailure(Call<MealsResponse> call, Throwable t) {

                mealOfTheDayCallback.onResultFailureMealOfTheDayCallback(t.getMessage());
            }
        });
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
}
