package com.example.foodplanner.meals.mainmealsfragment.presenter;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.remote.AllMealsCallback;
import com.example.foodplanner.data.remote.MealOfTheDayCallback;
import com.example.foodplanner.data.repository.RepositoryInterface;
import com.example.foodplanner.meals.mainmealsfragment.view.MealsFragmentViewInterface;

import java.util.Calendar;
import java.util.List;

public class MealsPresenter implements AllMealsCallback, MealsPresenterInterface, MealOfTheDayCallback,Parcelable {

    private final RepositoryInterface repository;
    private final MealsFragmentViewInterface viewInterface;


    private Meal mealToAdd;

    private Meal mealOfTheDay ;

    public List<Meal> getAllMeals() {
        return allMeals;
    }

    private List<Meal> allMeals ;


    public void setAllMeals(List<Meal> allMeals) {
        this.allMeals = allMeals;
    }

    public MealsPresenter(RepositoryInterface repository, MealsFragmentViewInterface viewInterface)
    {

        this.repository = repository;
        this.viewInterface = viewInterface;

    }



    public Meal getMealToAdd() {
        return mealToAdd;
    }

    public Meal getMealOfTheDay()
    {
        return mealOfTheDay;
    }

    public void setMealToAdd(Meal mealToAdd) {
        this.mealToAdd = mealToAdd;
    }


    public String  getCurrentDay()
    {
        return  String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    public void setMealOfTheDay(Meal mealOfTheDay) {
        this.mealOfTheDay = mealOfTheDay;
    }



    @Override
    public void onResultSuccessAllMealsCallback(List<Meal> meals) {
        this.allMeals = meals;
        viewInterface.onResultSuccessAllMealsCallback();
    }

    @Override
    public void onResultFailureAllMealsCallback(String error) {

        viewInterface.onResultFailureAllMealsCallback(error);
    }

    @Override
    public void getAllMealsRequest() {

        repository.getAllMealsResponse(this);
    }

    @Override
    public void mealOfTheDayRequest() {
        repository.getMealOfTheDay(this);
    }

    @Override
    public void insertMealToBreakfast(Meal meal) {
        repository.insertMealToBreakfast(meal,getCurrentDay());
    }

    @Override
    public void insertMealToLaunch(Meal meal) {

        repository.insertMealToLaunch(meal,getCurrentDay());
    }

    @Override
    public void insertMealToDinner(Meal meal) {

        repository.insertMealToDinner(meal,getCurrentDay());
    }

    @Override
    public void insertMealToFavourite(Meal meal) {

        repository.insertMealToFavourite(meal,getCurrentDay());
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
    }


}
