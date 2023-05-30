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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
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
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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
    public Observable<MealsResponse> searchByNameMealRequest(String prefix) {
        return retrofit.searchMealsByName(prefix);
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
    public void filterMealsByCountry(String country, SearchResultsPresenterInterface presenterInterface) {
        retrofit.filterMealsByCountry(country).enqueue(new Callback<FilterMealResponse>() {
            @Override
            public void onResponse(Call<FilterMealResponse> call, Response<FilterMealResponse> response) {
                presenterInterface.onFilterCountrySuccessCallback(response.body().getMeals());
            }

            @Override
            public void onFailure(Call<FilterMealResponse> call, Throwable t) {
                presenterInterface.onFilterCountryFailureCallback(t.getMessage());
            }
        });
    }

    @Override
    public void filterMealsByIngredient(String ingredients, SearchResultsPresenterInterface presenterInterface) {
        retrofit.filterMealsByIngredient(ingredients).enqueue(new Callback<FilterMealResponse>() {
            @Override
            public void onResponse(Call<FilterMealResponse> call, Response<FilterMealResponse> response) {
                presenterInterface.onFilterIngredientSuccessCallback(response.body().getMeals());
            }

            @Override
            public void onFailure(Call<FilterMealResponse> call, Throwable t) {

                presenterInterface.onFilterIngredientFailureCallback(t.getMessage());
            }
        });
    }

    @Override
    public void filterMealsByCategory(String category, SearchResultsPresenterInterface presenterInterface) {
     retrofit.filterMealsByCategory(category).enqueue(new Callback<FilterMealResponse>() {
         @Override
         public void onResponse(Call<FilterMealResponse> call, Response<FilterMealResponse> response) {
             presenterInterface.onFilterCategorySuccessCallback(response.body().getMeals());
         }

         @Override
         public void onFailure(Call<FilterMealResponse> call, Throwable t) {

             presenterInterface.onFilterCategoryFailureCallback(t.getMessage());
         }
     });
    }

    @Override
    public void getFullDetailsById(String id,String requester, SearchResultsPresenterInterface presenterInterface) {
        retrofit.getFullDetailsById(id).enqueue(new Callback<MealsResponse>() {
            @Override
            public void onResponse(Call<MealsResponse> call, Response<MealsResponse> response) {
                presenterInterface.onGetItemFullDetailsSuccessCallback(response.body().getMeals(),requester);
            }

            @Override
            public void onFailure(Call<MealsResponse> call, Throwable t) {

            }
        });
    }






    @Override
    public void getAllCategories(SearchSelectionPresenterInterface presenterInterface) {
       retrofit.getAllCategories().enqueue(new Callback<AllCategoriesResponse>() {
            @Override
            public void onResponse(Call<AllCategoriesResponse> call, Response<AllCategoriesResponse> response) {
                presenterInterface.onGetAllCategoriesSuccessCallback(response.body().getMealCategories());
            }

            @Override
            public void onFailure(Call<AllCategoriesResponse> call, Throwable t) {
                   presenterInterface.onGetAllCountriesFailureCallback(t.getMessage());
            }
        });
    }

    @Override
    public void getAllCountries(SearchSelectionPresenterInterface presenterInterface) {

        retrofit.getAllCountries().enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                presenterInterface.onGetAllCountriesSuccessCallback(response.body().getAreas());
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {

                presenterInterface.onGetAllCountriesFailureCallback(t.getMessage());
            }
        });

    }

    @Override
    public void getAllIngredients(SearchSelectionPresenterInterface presenterInterface) {

        retrofit.getAllIngredients().enqueue(new Callback<IngredientsResponse>() {
            @Override
            public void onResponse(Call<IngredientsResponse> call, Response<IngredientsResponse> response) {
                presenterInterface.onGetAllIngredientsSuccessCallback(response.body().getMeals());
            }

            @Override
            public void onFailure(Call<IngredientsResponse> call, Throwable t) {

                presenterInterface.onGetAllIngredientsFailureCallback(t.getMessage());
            }
        });
    }
}
