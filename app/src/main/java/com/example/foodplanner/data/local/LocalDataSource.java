package com.example.foodplanner.data.local;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;

import java.util.List;

public interface LocalDataSource {

    public void insertMealToBreakfast(Meal meal,String day);

    public void insertMealToLaunch(Meal meal,String day);

    public void insertMealToDinner(Meal meal,String day);

    public void insertMealToFavourite(Meal meal,String day);


    public void deleteMealFromBreakfast(Meal meal);
    public void deleteMealFromLaunch(Meal meal);
    public void deleteMealFromDinner(Meal meal);
    public void deleteMealFromFavourite(Meal meal);

    public LiveData<List<Favourite>> getAllFavouriteMeals();
    public LiveData<List<Breakfast>> getAllBreakfastMeals();
    public LiveData<List<Launch>> getAllLaunchMeals();
    public LiveData<List<Dinner>> getAllDinnerMeals();

    public  void clearAllTables();


}
