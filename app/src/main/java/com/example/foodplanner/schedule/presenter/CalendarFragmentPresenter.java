package com.example.foodplanner.schedule.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.data.dto.Day;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;
import com.example.foodplanner.data.repository.RepositoryInterface;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragmentPresenter {

    private RepositoryInterface repository;
    public CalendarFragmentPresenter(RepositoryInterface repository)
    {
        this.repository = repository;

    }


    public Day[] getWeekDays()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

        Day[] days = new Day[7];
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

        return days;
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


}