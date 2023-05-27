package com.example.foodplanner.data.dto;

import java.util.List;

public class FirebaseMealsResponse {

    public List<Meal> breakfast;
    public List<Meal> launch;
    public List<Meal> dinner;

    public List<Meal> getLaunches() {
        return launch;
    }

    public void setLaunches(List<Meal> launches) {
        this.launch = launches;
    }

    public List<Meal> getDinners() {
        return dinner;
    }

    public void setDinners(List<Meal> dinners) {
        this.dinner = dinners;
    }

    public List<Meal> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Meal> favourites) {
        this.favourites = favourites;
    }

    public List<Meal> favourites;

    public FirebaseMealsResponse()
    {

    }
    public List<Meal> getBreakfasts() {
        return breakfast;
    }

    public void setBreakfasts(List<Meal> breakfasts) {
        this.breakfast = breakfasts;
    }
}
