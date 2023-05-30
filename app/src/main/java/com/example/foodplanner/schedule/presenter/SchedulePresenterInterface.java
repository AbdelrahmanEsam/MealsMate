package com.example.foodplanner.schedule.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface SchedulePresenterInterface {

   Observable<List<Breakfast>> getAllBreakfastMeals();
   Observable<List<Launch>>  getAllLaunchMeals();
   Observable<List<Dinner>> getAllDinnerMeals();
   Observable<List<Favourite>>  getAllFavouriteMeals();

   void syncDataWithCloud(List<Meal> breakfasts,List<Meal> launches,List<Meal> dinners,List<Meal> favourites);

   Completable deleteBreakfastItem(Meal meal);
   Completable deleteLaunchItem(Meal meal);
   Completable deleteDinnerItem(Meal meal);
   Completable deleteFavouritesItem(Meal meal);
}
