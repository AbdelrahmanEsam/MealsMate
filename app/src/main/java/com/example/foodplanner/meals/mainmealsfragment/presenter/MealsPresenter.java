package com.example.foodplanner.meals.mainmealsfragment.presenter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.meal.MealsResponse;
import com.example.foodplanner.data.repository.RepositoryInterface;
import com.example.foodplanner.meals.mainmealsfragment.view.MealOfTheDayCallback;
import com.example.foodplanner.meals.mainmealsfragment.view.MealsFragmentViewInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsPresenter implements MealsPresenterInterface, MealOfTheDayCallback,Parcelable {

    private final RepositoryInterface repository;
    private MealsFragmentViewInterface viewInterface;


    private Meal mealToAdd;

    private Meal mealOfTheDay ;

    private String searchPrefix;

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public void setDisposable(CompositeDisposable disposable) {
        this.disposable = disposable;
    }

    private CompositeDisposable disposable = new CompositeDisposable();

    public String getSearchPrefix() {
        return searchPrefix;
    }

    public void setSearchPrefix(String searchPrefix) {
        this.searchPrefix = searchPrefix;
    }

    private List<Meal> allMeals;






    public MealsPresenter(RepositoryInterface repository, MealsFragmentViewInterface viewInterface)
    {

        this.repository = repository;
        this.viewInterface = viewInterface;
        allMeals = new ArrayList<>();

    }

    public MealsFragmentViewInterface getViewInterface() {
        return viewInterface;
    }

    public void setViewInterface(MealsFragmentViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }
    public List<Meal> getAllMeals() {
        return allMeals;
    }

    public void setAllMeals(List<Meal> allMeals) {
        this.allMeals = allMeals;
    }
    public Meal getMealToAdd() {
        return mealToAdd;
    }

    public void setMealToAdd(Meal mealToAdd) {
        this.mealToAdd = mealToAdd;
    }


    public Meal getMealOfTheDay()
    {
        return mealOfTheDay;
    }




    public String  getCurrentDay()
    {
        return  String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    public void setMealOfTheDay(Meal mealOfTheDay) {
        this.mealOfTheDay = mealOfTheDay;
    }



    @Override
    public void mealOfTheDayRequest() {
        repository.getMealOfTheDay(this);
    }

    @Override
    public void searchByNameMealRequest(String prefix) {


       repository.searchByNameMealRequest(prefix).subscribeOn(Schedulers.io())
                .distinctUntilChanged().observeOn(AndroidSchedulers.mainThread()).subscribe(mealsResponse -> {
                    allMeals.clear();
                    if (mealsResponse.getMeals() != null){
                        allMeals.addAll(mealsResponse.getMeals());
                    }else{
                        allMeals.clear();
                    }
                    viewInterface.onSearchSuccessResult();
                });
    }




    @Override
    public Completable insertMealToBreakfast(Meal meal) {
      return   repository.insertMealToBreakfast(meal);
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

       return repository.insertMealToFavourite(meal);
    }

    @Override
    public void onResultSuccessMealOfTheDayCallback(Meal meal) {

        mealOfTheDay = meal;
        viewInterface.onResultSuccessOneMealsCallback();
    }

    @Override
    public void onResultFailureMealOfTheDayCallback(String error) {
        viewInterface.onResultFailureOneMealCallback(error);
    }




    protected MealsPresenter(Parcel in) {
        repository = in.readParcelable(RepositoryInterface.class.getClassLoader());
        viewInterface = in.readParcelable(MealsFragmentViewInterface.class.getClassLoader());
        mealToAdd = in.readParcelable(Meal.class.getClassLoader());
        mealOfTheDay = in.readParcelable(Meal.class.getClassLoader());
        allMeals = in.createTypedArrayList(Meal.CREATOR);
        searchPrefix = in.readString();

    }

    public static final Creator<MealsPresenter> CREATOR = new Creator<MealsPresenter>() {
        @Override
        public MealsPresenter createFromParcel(Parcel in) {
            return new MealsPresenter(in);
        }

        @Override
        public MealsPresenter[] newArray(int size) {
            return new MealsPresenter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mealToAdd, flags);
        dest.writeParcelable(mealOfTheDay, flags);
        dest.writeTypedList(allMeals);
        dest.writeString(searchPrefix);
    }





}
