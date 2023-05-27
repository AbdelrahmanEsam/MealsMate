package com.example.foodplanner.schedule.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;

import java.util.List;

public interface SchedulePresenterInterface {

   LiveData<List<Breakfast>> getAllBreakfastMeals();
   LiveData<List<Launch>>  getAllLaunchMeals();
   LiveData<List<Dinner>> getAllDinnerMeals();
   LiveData<List<Favourite>>  getAllFavouriteMeals();

   void syncDataWithCloud(List<Meal> breakfasts,List<Meal> launches,List<Meal> dinners,List<Meal> favourites);

   void deleteBreakfastItem(Meal meal);
   void deleteLaunchItem(Meal meal);
   void deleteDinnerItem(Meal meal);
   void deleteFavouritesItem(Meal meal);
}
