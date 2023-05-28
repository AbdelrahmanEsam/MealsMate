package com.example.foodplanner.meals.search.searchresults.presenter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.search.FilterMeal;
import com.example.foodplanner.data.repository.RepositoryInterface;
import com.example.foodplanner.meals.search.searchresults.view.SearchResultsViewInterface;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsPresenter implements SearchResultsPresenterInterface, Parcelable {

    private RepositoryInterface repository;

    private SearchResultsViewInterface viewInterface;

    private List<FilterMeal>  results = new ArrayList<>();

    private Meal navigateToDetailsMeal = null ;

    public Meal getNavigateToDetailsMeal() {
        return navigateToDetailsMeal;
    }

    public void setNavigateToDetailsMeal(Meal navigateToDetailsMeal) {
        this.navigateToDetailsMeal = navigateToDetailsMeal;
    }

    public SearchResultsPresenter(RepositoryInterface repository, SearchResultsViewInterface viewInterface) {
        this.repository = repository;
        this.viewInterface = viewInterface;

    }

    public List<FilterMeal> getResults() {
        return results;
    }

    public void setResults(List<FilterMeal> results) {
        this.results = results;
    }

    protected SearchResultsPresenter(Parcel in) {
        results = in.createTypedArrayList(FilterMeal.CREATOR);
        navigateToDetailsMeal = in.readParcelable(Meal.class.getClassLoader());
    }

    public static final Creator<SearchResultsPresenter> CREATOR = new Creator<SearchResultsPresenter>() {
        @Override
        public SearchResultsPresenter createFromParcel(Parcel in) {
            return new SearchResultsPresenter(in);
        }

        @Override
        public SearchResultsPresenter[] newArray(int size) {
            return new SearchResultsPresenter[size];
        }
    };




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
        dest.writeParcelable(navigateToDetailsMeal, flags);
    }




    @Override
    public void filterMealsByCategory(String category) {

        repository.filterMealsByCategory(category,this);
    }

    @Override
    public void filterMealsByIngredient(String ingredient) {

        repository.filterMealsByIngredient(ingredient,this);
    }

    @Override
    public void filterMealsByCountry(String country) {
        Log.d("searchResult","request from presenter");
        repository.filterMealsByCountry(country,this);
    }

    @Override
    public void getFullDetailsMealRequest(String id) {
        repository.getFullDetailsById(id,this);
    }

    @Override
    public void onFilterCategorySuccessCallback(List<FilterMeal> categories) {
        results.addAll(categories);
        viewInterface.onResultsSuccessCallback(results);
    }

    @Override
    public void onFilterIngredientSuccessCallback(List<FilterMeal> ingredients) {
        results.addAll(ingredients);
        viewInterface.onResultsSuccessCallback(results);
    }

    @Override
    public void onFilterCountrySuccessCallback(List<FilterMeal> countries) {
        results.addAll(countries);
        viewInterface.onResultsSuccessCallback(results);
    }

    @Override
    public void onGetItemFullDetailsSuccessCallback(List<Meal> meals) {
        navigateToDetailsMeal = meals.get(0);
        viewInterface.onGetDetailsSuccessCallback(navigateToDetailsMeal);
    }

    @Override
    public void onFilterCategoryFailureCallback(String error) {

        viewInterface.onResultsFailureCallback(error);
    }

    @Override
    public void onFilterIngredientFailureCallback(String error) {

        viewInterface.onResultsFailureCallback(error);
    }

    @Override
    public void onFilterCountryFailureCallback(String error) {

        viewInterface.onResultsFailureCallback(error);
    }
}
