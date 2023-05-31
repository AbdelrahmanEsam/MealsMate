package com.example.foodplanner.schedule.presenter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.Day;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;
import com.example.foodplanner.data.repository.RepositoryInterface;
import com.example.foodplanner.schedule.view.ScheduleFragment;
import com.example.foodplanner.schedule.view.ScheduleViewInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SchedulePresenter implements  SchedulePresenterInterface, Parcelable {

    private final RepositoryInterface repository;

    private List<Breakfast> presenterBreakfasts ;
    private List<Launch> presenterLaunches ;
    private List<Dinner> presenterDinners ;
    private List<Favourite> presenterFavourites ;

    private ScheduleViewInterface viewInterface ;

    public List<Breakfast> getPresenterBreakfasts() {
        return presenterBreakfasts;
    }

    public void setPresenterBreakfasts(List<Breakfast> presenterBreakfasts) {
        this.presenterBreakfasts = presenterBreakfasts;
    }

    public List<Launch> getPresenterLaunches() {
        return presenterLaunches;
    }

    public void setPresenterLaunches(List<Launch> presenterLaunches) {
        this.presenterLaunches = presenterLaunches;
    }

    public List<Dinner> getPresenterDinners() {
        return presenterDinners;
    }

    public void setPresenterDinners(List<Dinner> presenterDinners) {
        this.presenterDinners = presenterDinners;
    }

    public List<Favourite> getPresenterFavourites() {
        return presenterFavourites;
    }

    public void setPresenterFavourites(List<Favourite> presenterFavourites) {
        this.presenterFavourites = presenterFavourites;
    }

    private  Day[] days;
    public SchedulePresenter(RepositoryInterface repository,ScheduleViewInterface viewInterface)
    {
        this.repository = repository;
        this.viewInterface = viewInterface;
        presenterBreakfasts = new ArrayList<>();
        presenterLaunches = new ArrayList<>();
        presenterDinners = new ArrayList<>();
        presenterFavourites = new ArrayList<>();
    }





    public Day[] getWeekDays()
    {
        if (days == null){
            days = new Day[7];
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.SATURDAY);
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

       Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
      String day  = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        return day;
    }

    public void getAllBreakfastMeals() {

        repository.getAllBreakfastMeals().debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Breakfast>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<Breakfast> breakfasts) {


                if (!breakfasts.isEmpty())
                {
                    Log.d("breakfast",breakfasts.size()+"");
                    presenterBreakfasts.clear();
                    presenterBreakfasts.addAll(breakfasts);
                    viewInterface.onBreakfastsSuccessCallback();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    public void getAllLaunchMeals() {


        repository.getAllLaunchMeals().debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Launch>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<Launch> launches) {


                if (!launches.isEmpty())
                {
                    presenterLaunches.clear();
                    presenterLaunches.addAll(launches);
                    viewInterface.onLaunchesSuccessCallback();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    public void getAllDinnerMeals() {

        repository.getAllDinnerMeals().debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Dinner>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<Dinner> dinners) {


                if (!dinners.isEmpty())
                {
                    presenterDinners.clear();
                    presenterDinners.addAll(dinners);
                    viewInterface.onDinnersSuccessCallback();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getAllFavouriteMeals() {

        repository.getAllFavouriteMeals().debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Favourite>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<Favourite> favourites) {


                if (!favourites.isEmpty())
                {
                    presenterFavourites.clear();
                   presenterFavourites.addAll(favourites);
                   viewInterface.onFavouritesSuccessCallback();

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void syncDataWithCloud(List<Meal> breakfasts, List<Meal> launches, List<Meal> dinner, List<Meal> favourites) {
        repository.clearAllTables();
        breakfasts.forEach(meal -> {
            if (meal != null) {
                repository.insertMealToBreakfast(meal).subscribeOn(Schedulers.io()).subscribe();
            }
        });

        launches.forEach(meal -> {
            if (meal != null) {
                repository.insertMealToLaunch(meal).subscribeOn(Schedulers.io()).subscribe();
            }
        });


        dinner.forEach(meal -> {
            if (meal != null) {
                repository.insertMealToDinner(meal).subscribeOn(Schedulers.io()).subscribe();
            }
        });


        favourites.forEach(meal -> {
            if (meal != null) {
                repository.insertMealToFavourite(meal).subscribeOn(Schedulers.io()).subscribe();
            }
        });
    }

    @Override
    public Completable deleteBreakfastItem(Meal meal) {
      return   repository.deleteMealFromBreakfast(meal);
    }

    @Override
    public Completable deleteLaunchItem(Meal meal) {

      return   repository.deleteMealFromLaunch(meal);
    }

    @Override
    public Completable deleteDinnerItem(Meal meal) {

      return   repository.deleteMealFromDinner(meal);
    }

    @Override
    public Completable deleteFavouritesItem(Meal meal) {

       return repository.deleteMealFromFavourite(meal);
    }


}
