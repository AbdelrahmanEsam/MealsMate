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
    private MealsDatabase database;
    private  BreakfastDao breakfastDao ;
    private  LaunchDao launchDao ;
    private  DinnerDao dinnerDao ;
    private  FavouriteDao favouriteDao ;
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
       Breakfast breakfast =  meal.mealToBreakfastMapper();
       breakfast.setDay(day);
        breakfastDao.insert(breakfast);
    }

    @Override
    public void insertMealToLaunch(Meal meal) {

        launchDao.insert(meal.mealToLaunchMapper());
    }

    @Override
    public void insertMealToDinner(Meal meal) {

        dinnerDao.insert(meal.mealToDinnerMapper());

    }

    @Override
    public void insertMealToFavourite(Meal meal) {

        favouriteDao.insert(meal.mealToFavouriteMapper());
    }

    @Override
    public void deleteMealFromBreakfast(Meal meal) {
        breakfastDao.delete(meal.mealToBreakfastMapper());
    }

    @Override
    public void deleteMealFromLaunch(Meal meal) {
        launchDao.delete(meal.mealToLaunchMapper());
    }

    @Override
    public void deleteMealFromDinner(Meal meal) {
        dinnerDao.delete(meal.mealToDinnerMapper());
    }

    @Override
    public void deleteMealFromFavourite(Meal meal) {
        favouriteDao.delete(meal.mealToFavouriteMapper());
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
}
