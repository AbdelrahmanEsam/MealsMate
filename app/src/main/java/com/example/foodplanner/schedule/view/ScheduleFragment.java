package com.example.foodplanner.schedule.view;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.data.dto.Day;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;
import com.example.foodplanner.data.local.LocalDataSourceImp;
import com.example.foodplanner.data.remote.RemoteDataSourceImpl;
import com.example.foodplanner.data.repository.Repository;
import com.example.foodplanner.databinding.FragmentScheduleBinding;
import com.example.foodplanner.meals.mainmealsfragment.view.OnMealClickListener;
import com.example.foodplanner.schedule.presenter.CalendarFragmentPresenter;

import java.util.Arrays;
import java.util.stream.Collectors;


public class ScheduleFragment extends Fragment implements OnDayClicked, OnMealClickListener {

    private FragmentScheduleBinding binding;
    private NavController controller;
    private CalendarFragmentPresenter presenter;
    private final ScheduleMealsAdapter breakfastAdapter = new ScheduleMealsAdapter();
    private final ScheduleMealsAdapter launchAdapter = new ScheduleMealsAdapter();
    private final ScheduleMealsAdapter dinnerAdapter = new ScheduleMealsAdapter();
    private final ScheduleMealsAdapter favouriteAdapter = new ScheduleMealsAdapter();
    private final DaysAdapter daysAdapter = new DaysAdapter();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        presenter = new CalendarFragmentPresenter(Repository.getInstance(RemoteDataSourceImpl.getInstance(), LocalDataSourceImp.getInstance(getContext())));


        breakfastMealsObservers();
        launchMealsObservers();
        dinnerMealsObservers();
        favouritesMealsObservers();
        setDaysAdapter();
        setRecyclerView(binding.daysRecycler,daysAdapter);
        setRecyclerView(binding.breakFastRecycler,breakfastAdapter);
        setRecyclerView(binding.launchRecycler,launchAdapter);
        setRecyclerView(binding.dinnerRecycler,dinnerAdapter);
        setRecyclerView(binding.favouritesRecycler,favouriteAdapter);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void breakfastMealsObservers()
    {
        presenter.getAllBreakfastMeals().observe(getViewLifecycleOwner(),breakfasts -> {
            breakfastAdapter.setMeals(breakfasts.stream().map(Breakfast::breakFastToMealMapper).collect(Collectors.toList()), getContext(),this);
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void launchMealsObservers()
    {
        presenter.getAllLaunchMeals().observe(getViewLifecycleOwner(),launches -> {
            launchAdapter.setMeals(launches.stream().map(Launch::launchToMealMapper).collect(Collectors.toList()), getContext(),this);
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dinnerMealsObservers()
    {
        presenter.getAllDinnerMeals().observe(getViewLifecycleOwner(),dinners -> {
            Log.d("dinners",dinners.size()+"");
            dinnerAdapter.setMeals(dinners.stream().map(Dinner::dinnerToMealMapper).collect(Collectors.toList()), getContext(),this);
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void favouritesMealsObservers()
    {
        presenter.getAllFavouriteMeals().observe(getViewLifecycleOwner(),favourites -> {
            favouriteAdapter.setMeals(favourites.stream().map(Favourite::favouriteToMealMapper).collect(Collectors.toList()), getContext(),this);
        });

    }


    private void setRecyclerView(RecyclerView recyclerView,RecyclerView.Adapter adapter)
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setDaysAdapter()
    {
        daysAdapter.setDays(Arrays.asList(presenter.getWeekDays()),getContext(),this);
    }






    @Override
    public void onDayClicked(Day day,int prevPosition) {

        Log.d("position",prevPosition+"");
        daysAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMealClickListener(Meal meal, ImageView transitionView) {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(transitionView, transitionView.getTransitionName())
                .build();
        controller.navigate(ScheduleFragmentDirections.actionScheduleFragmentToMealDetailsFragment(transitionView.getTransitionName(),meal),extras);
    }
}