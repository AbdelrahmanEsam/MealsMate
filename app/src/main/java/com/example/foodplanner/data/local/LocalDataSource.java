package com.example.foodplanner.data.local;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface LocalDataSource {

    public Completable insertMealToBreakfast(Meal meal);

    public Completable  insertMealToLaunch(Meal meal);

    public Completable  insertMealToDinner(Meal meal);

    public Completable  insertMealToFavourite(Meal meal);


    public Completable  deleteMealFromBreakfast(Meal meal);
    public Completable  deleteMealFromLaunch(Meal meal);
    public Completable  deleteMealFromDinner(Meal meal);
    public Completable  deleteMealFromFavourite(Meal meal);

    public Observable<List<Favourite>> getAllFavouriteMeals();
    public Observable<List<Breakfast>> getAllBreakfastMeals();
    public Observable<List<Launch>> getAllLaunchMeals();
    public Observable<List<Dinner>> getAllDinnerMeals();

    public  void clearAllTables();


}
