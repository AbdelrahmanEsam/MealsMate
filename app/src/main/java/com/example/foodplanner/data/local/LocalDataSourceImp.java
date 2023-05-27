package com.example.foodplanner.data.local;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;

import java.util.List;

public class LocalDataSourceImp implements  LocalDataSource{

    private static  LocalDataSourceImp localDataSourceImp;
    private final MealsDatabase database;
    private final BreakfastDao breakfastDao ;
    private final LaunchDao launchDao ;
    private final DinnerDao dinnerDao ;
    private final FavouriteDao favouriteDao ;
    private LocalDataSourceImp(Context context)
    {
        database = MealsDatabase.getInstance(context);

        this.breakfastDao = database.BreakfastDao();
        this.launchDao = database.LaunchDao();
        this.dinnerDao = database.DinnerDao();
        this.favouriteDao = database.FavouriteDao();

    }


    public static synchronized LocalDataSourceImp getInstance(Context context)
    {

        if (localDataSourceImp == null)
        {
            localDataSourceImp = new LocalDataSourceImp(context);
        }

        return localDataSourceImp;
    }

    @Override
    public void insertMealToBreakfast(Meal meal,String day) {
        new Thread(() -> {
                Breakfast breakfast = meal.mealToBreakfastMapper();
                breakfast.setDay(day);
                breakfastDao.insert(breakfast);
        }).start();

    }

    @Override
    public void insertMealToLaunch(Meal meal,String day) {

        new Thread(() -> {
                launchDao.insert(meal.mealToLaunchMapper());
        }).start();

    }

    @Override
    public void insertMealToDinner(Meal meal,String day) {
        new Thread(() -> {
                dinnerDao.insert(meal.mealToDinnerMapper());
        }).start();


    }

    @Override
    public void insertMealToFavourite(Meal meal,String day) {
        new Thread(() -> {
            favouriteDao.insert(meal.mealToFavouriteMapper());
        }).start();

    }

    @Override
    public void deleteMealFromBreakfast(Meal meal) {

        new Thread(() -> {
            breakfastDao.delete(meal.mealToBreakfastMapper());
        }).start();
    }

    @Override
    public void deleteMealFromLaunch(Meal meal) {


        new Thread(() -> {
            launchDao.delete(meal.mealToLaunchMapper());
        }).start();
    }

    @Override
    public void deleteMealFromDinner(Meal meal) {


        new Thread(() -> {
            dinnerDao.delete(meal.mealToDinnerMapper());
        }).start();
    }

    @Override
    public void deleteMealFromFavourite(Meal meal) {


        new Thread(() -> {
            favouriteDao.delete(meal.mealToFavouriteMapper());
        }).start();
    }

    @Override
    public LiveData<List<Favourite>> getAllFavouriteMeals() {
        return favouriteDao.getAllMeals();
    }

    @Override
    public LiveData<List<Breakfast>> getAllBreakfastMeals() {
        return  breakfastDao.getAllMeals();
    }

    @Override
    public LiveData<List<Launch>> getAllLaunchMeals() {
        return launchDao.getAllMeals();
    }

    @Override
    public LiveData<List<Dinner>> getAllDinnerMeals() {
        return  dinnerDao.getAllMeals();
    }

    @Override
    public void clearAllTables() {
        new Thread(() -> {
            database.clearAllTables();
        }).start();
    }
}
