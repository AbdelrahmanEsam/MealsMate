package com.example.foodplanner.meals.search.searchresults.presenter;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.search.FilterMeal;
import com.example.foodplanner.data.repository.RepositoryInterface;
import com.example.foodplanner.meals.search.searchresults.view.SearchResultsViewInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchResultsPresenter implements SearchResultsPresenterInterface, Parcelable {

    private RepositoryInterface repository;

    private SearchResultsViewInterface viewInterface;

    private List<FilterMeal>  results = new ArrayList<>();

    private Meal fullDetailedMeal = null ;



    public Meal getFullDetailedMeal() {
        return fullDetailedMeal;
    }

    public void setFullDetailedMeal(Meal fullDetailedMeal) {
        this.fullDetailedMeal = fullDetailedMeal;
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
        fullDetailedMeal = in.readParcelable(Meal.class.getClassLoader());
    }


    public String  getCurrentDay()
    {
        return  String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
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
        dest.writeParcelable(fullDetailedMeal, flags);
    }




    @SuppressLint("CheckResult")
    @Override
    public void filterMealsByCategory(String category) {

        repository.filterMealsByCategory(category).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(filterMealResponse -> {
                    results.addAll(filterMealResponse.getMeals());
                    viewInterface.onResultsSuccessCallback(results);
                },throwable -> {
                    viewInterface.onResultsFailureCallback(throwable.getMessage());
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void filterMealsByIngredient(String ingredient) {

        repository.filterMealsByIngredient(ingredient).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(filterMealResponse -> {
                    results.addAll(filterMealResponse.getMeals());
                    viewInterface.onResultsSuccessCallback(results);
        },throwable -> {
                    viewInterface.onResultsFailureCallback(throwable.getMessage());
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void filterMealsByCountry(String country) {
        repository.filterMealsByCountry(country).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(filterMealResponse -> {
                    results.addAll(filterMealResponse.getMeals());
                    viewInterface.onResultsSuccessCallback(results);
                },throwable -> {
                    viewInterface.onResultsFailureCallback(throwable.getMessage());
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void getFullDetailsMealRequest(String id,String requester) {
        repository.getFullDetailsById(id,requester).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(mealsResponse -> {
                    fullDetailedMeal = mealsResponse.getMeals().get(0);
                    viewInterface.onGetDetailsSuccessCallback(fullDetailedMeal,requester);
                },throwable -> {
                    viewInterface.onGetDetailsFailureCallback(throwable.getMessage());
                });
    }

    @Override
    public Completable insertMealToBreakfast(Meal meal) {
       return repository.insertMealToBreakfast(meal);
    }

    @Override
    public Completable insertMealToLaunch(Meal meal) {

      return   repository.insertMealToLaunch(meal);
    }

    @Override
    public Completable insertMealToDinner(Meal meal) {

      return   repository.insertMealToDinner(meal);
    }

    @Override
    public Completable insertMealToFavourite(Meal meal) {

      return   repository.insertMealToFavourite(meal);
    }
}
