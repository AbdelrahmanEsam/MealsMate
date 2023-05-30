package com.example.foodplanner.meals.detailsfragment.presenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.repository.RepositoryInterface;

import java.util.Calendar;

import io.reactivex.rxjava3.core.Completable;

public class MealDetailsPresenter implements  MealDetailsPresenterInterface, Parcelable {


    private final RepositoryInterface repository;



    private Meal mealToAdd;

    public Meal getMealToAdd() {
        return mealToAdd;
    }

    public void setMealToAdd(Meal mealToAdd) {
        this.mealToAdd = mealToAdd;
    }

    public MealDetailsPresenter(RepositoryInterface repository)
    {

        this.repository = repository;


    }

    public String  getCurrentDay()
    {
        return  String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }


    protected MealDetailsPresenter(Parcel in) {
        repository = in.readParcelable(RepositoryInterface.class.getClassLoader());
        mealToAdd = in.readParcelable(Meal.class.getClassLoader());
    }

    public static final Creator<MealDetailsPresenter> CREATOR = new Creator<MealDetailsPresenter>() {
        @Override
        public MealDetailsPresenter createFromParcel(Parcel in) {
            return new MealDetailsPresenter(in);
        }

        @Override
        public MealDetailsPresenter[] newArray(int size) {
            return new MealDetailsPresenter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mealToAdd, flags);
    }


    @Override
    public Completable insertMealToBreakfast(Meal meal) {
      return   repository.insertMealToBreakfast(meal,getCurrentDay());
    }

    @Override
    public Completable insertMealToLaunch(Meal meal) {
      return   repository.insertMealToLaunch(meal,getCurrentDay());
    }

    @Override
    public Completable insertMealToDinner(Meal meal) {
       return repository.insertMealToDinner(meal,getCurrentDay());
    }

    @Override
    public Completable insertMealToFavourite(Meal meal) {
       return repository.insertMealToFavourite(meal,getCurrentDay());
    }
}
