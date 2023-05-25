package com.example.foodplanner.meals.mainmealsfragment.presenter;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.remote.AllMealsCallback;
import com.example.foodplanner.data.remote.MealOfTheDayCallback;
import com.example.foodplanner.data.repository.RepositoryInterface;
import com.example.foodplanner.meals.mainmealsfragment.view.MealsFragmentViewInterface;

import java.util.Calendar;
import java.util.List;

public class MealsPresenter implements AllMealsCallback, MealsPresenterInterface, MealOfTheDayCallback {

    private RepositoryInterface repository;
    private MealsFragmentViewInterface viewInterface;

    public MealsPresenter(RepositoryInterface repository, MealsFragmentViewInterface viewInterface)
    {

        this.repository = repository;
        this.viewInterface = viewInterface;

    }


    public String  getCurrentDay()
    {
        return  String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }





    @Override
    public void onResultSuccessAllMealsCallback(List<Meal> meals) {
        viewInterface.onResultSuccessAllMealsCallback(meals);
    }

    @Override
    public void onResultFailureAllMealsCallback(String error) {

        viewInterface.onResultFailureAllMealsCallback(error);
    }

    @Override
    public void getAllMeals() {

        repository.getAllMealsResponse(this);
    }

    @Override
    public void getMealOfTheDay() {
        repository.getMealOfTheDay(this);
    }

    @Override
    public void insertMealToBreakfast(Meal meal) {
        repository.insertMealToBreakfast(meal,getCurrentDay());
    }

    @Override
    public void insertMealToLaunch(Meal meal) {

        repository.insertMealToLaunch(meal);
    }

    @Override
    public void insertMealToDinner(Meal meal) {

        repository.insertMealToDinner(meal);
    }

    @Override
    public void insertMealToFavourite(Meal meal) {

        repository.insertMealToFavourite(meal);
    }

    @Override
    public void onResultSuccessMealOfTheDayCallback(Meal meal) {

        viewInterface.onResultSuccessOneMealsCallback(meal);
    }

    @Override
    public void onResultFailureMealOfTheDayCallback(String error) {

        viewInterface.onResultFailureOneMealCallback(error);
    }
}