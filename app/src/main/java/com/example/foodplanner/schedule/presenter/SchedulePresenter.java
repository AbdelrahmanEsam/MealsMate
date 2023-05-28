package com.example.foodplanner.schedule.presenter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.data.dto.Day;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;
import com.example.foodplanner.data.repository.RepositoryInterface;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SchedulePresenter implements  SchedulePresenterInterface, Parcelable {

    private final RepositoryInterface repository;

    private  Day[] days;
    public SchedulePresenter(RepositoryInterface repository)
    {
        this.repository = repository;
    }





    public Day[] getWeekDays()
    {
        if (days == null){
            days = new Day[7];
            Log.d("day","not lol");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            for (int i = 0; i < 7; i++)
            {
                Day day = new Day();
                String dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                day.setDayName(dayName);
                day.setDayNumber(String.valueOf(dayOfMonth));
                days[i] = day;
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            days[0].setSelected(true);
        }else{
            Log.d("day","lol");
        }
        return this.days;
    }


    protected SchedulePresenter(Parcel in) {
        repository = in.readParcelable(RepositoryInterface.class.getClassLoader());
        days = in.createTypedArray(Day.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(days, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SchedulePresenter> CREATOR = new Creator<SchedulePresenter>() {
        @Override
        public SchedulePresenter createFromParcel(Parcel in) {
            return new SchedulePresenter(in);
        }

        @Override
        public SchedulePresenter[] newArray(int size) {
            return new SchedulePresenter[size];
        }
    };

    public String getCurrentDay() {
        return String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    public LiveData<List<Breakfast>> getAllBreakfastMeals() {
       return repository.getAllBreakfastMeals();
    }


    public LiveData<List<Launch>> getAllLaunchMeals() {
       return repository.getAllLaunchMeals();
    }


    public LiveData<List<Dinner>> getAllDinnerMeals() {
        return repository.getAllDinnerMeals();
    }

    public LiveData<List<Favourite>> getAllFavouriteMeals() {
       return repository.getAllFavouriteMeals();
    }

    @Override
    public void syncDataWithCloud(List<Meal> breakfasts, List<Meal> launches, List<Meal> dinner, List<Meal> favourites) {
        repository.clearAllTables();
        breakfasts.forEach(meal -> {
            if (meal != null) {
                repository.insertMealToBreakfast(meal, getCurrentDay());
            }
        });

        launches.forEach(meal -> {
            if (meal != null) {
                repository.insertMealToLaunch(meal, getCurrentDay());
            }
        });


        dinner.forEach(meal -> {
            if (meal != null) {
                repository.insertMealToDinner(meal, getCurrentDay());
            }
        });


        favourites.forEach(meal -> {
            if (meal != null) {
                repository.insertMealToFavourite(meal, getCurrentDay());
            }
        });
    }

    @Override
    public void deleteBreakfastItem(Meal meal) {
        repository.deleteMealFromBreakfast(meal);
    }

    @Override
    public void deleteLaunchItem(Meal meal) {

        repository.deleteMealFromLaunch(meal);
    }

    @Override
    public void deleteDinnerItem(Meal meal) {

        repository.deleteMealFromDinner(meal);
    }

    @Override
    public void deleteFavouritesItem(Meal meal) {

        repository.deleteMealFromFavourite(meal);
    }


}
