package com.example.foodplanner.schedule.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieDrawable;
import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.Day;
import com.example.foodplanner.data.dto.FirebaseMealsResponse;
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
import com.example.foodplanner.schedule.presenter.SchedulePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ScheduleFragment extends Fragment implements OnDayListener, OnMealClickListener {

    private FragmentScheduleBinding binding;
    private NavController controller;
    private SchedulePresenter presenter;

    private FirebaseAuth mAuth;
    private final ScheduleMealsAdapter breakfastAdapter = new ScheduleMealsAdapter();
    private final ScheduleMealsAdapter launchAdapter = new ScheduleMealsAdapter();
    private final ScheduleMealsAdapter dinnerAdapter = new ScheduleMealsAdapter();
    private final ScheduleMealsAdapter favouriteAdapter = new ScheduleMealsAdapter();
    private final DaysAdapter daysAdapter = new DaysAdapter();

    private FirebaseFirestore firebaseFirestore;
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        presenter = new SchedulePresenter(Repository.getInstance(RemoteDataSourceImpl.getInstance(), LocalDataSourceImp.getInstance(getContext())));


        breakfastMealsObservers();
        launchMealsObservers();
        dinnerMealsObservers();
        favouritesMealsObservers();
        setDaysAdapter();
        binding.goodMorningTextView.append(mAuth.getCurrentUser().getDisplayName());
        binding.syncLottie.setOnClickListener(view1 -> {
            binding.syncLottie.setRepeatMode(LottieDrawable.RESTART);
            binding.syncLottie.playAnimation();

            syncCloudWithCache();
        });
        setRecyclerView(binding.daysRecycler,daysAdapter);
        setRecyclerView(binding.breakFastRecycler,breakfastAdapter);
        setRecyclerView(binding.launchRecycler,launchAdapter);
        setRecyclerView(binding.dinnerRecycler,dinnerAdapter);
        setRecyclerView(binding.favouritesRecycler,favouriteAdapter);

    }


    private void syncCloudWithCache()
    {


        firebaseFirestore.collection(getString(R.string.users)).document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Object breakfasts = document.get(getString(R.string.breakfast));

                        List<Map<String, Object>> levels = (List<Map<String, Object>>) document.get(getString(R.string.breakfast));
                        levels.forEach(stringObjectMap -> {
                         Log.d("breakfast",  stringObjectMap.get("0").toString());

                        });


                    }
                }
            }
        });



    }


    private void breakfastMealsObservers()
    {
        presenter.getAllBreakfastMeals().observe(getViewLifecycleOwner(),breakfasts -> {
            breakfastAdapter.setMeals(breakfasts.stream().map(Breakfast::breakFastToMealMapper).collect(Collectors.toList()), getContext(),this);
        });

    }


    private void launchMealsObservers()
    {
        presenter.getAllLaunchMeals().observe(getViewLifecycleOwner(),launches -> {
            launchAdapter.setMeals(launches.stream().map(Launch::launchToMealMapper).collect(Collectors.toList()), getContext(),this);
        });

    }


    private void dinnerMealsObservers()
    {
        presenter.getAllDinnerMeals().observe(getViewLifecycleOwner(),dinners -> {
            Log.d("dinners",dinners.size()+"");
            dinnerAdapter.setMeals(dinners.stream().map(Dinner::dinnerToMealMapper).collect(Collectors.toList()), getContext(),this);
        });

    }


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
    public void onMealClicked(Meal meal, ImageView transitionView) {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(transitionView, transitionView.getTransitionName())
                .build();
        controller.navigate(ScheduleFragmentDirections.actionScheduleFragmentToMealDetailsFragment(transitionView.getTransitionName(),meal),extras);
    }
}