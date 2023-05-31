package com.example.foodplanner.schedule.view;

import static androidx.recyclerview.widget.RecyclerView.*;
import static com.example.foodplanner.R.string.breakfast;
import static com.example.foodplanner.R.string.dinner;
import static com.example.foodplanner.R.string.favourites;
import static com.example.foodplanner.R.string.launch;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieDrawable;
import com.example.foodplanner.MainActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.Day;
import com.example.foodplanner.data.dto.firebase.FirebaseMealsResponse;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ScheduleFragment extends Fragment implements OnDayListener, OnMealClickListener,OnMealLongClickedListener,ScheduleViewInterface {

    private FragmentScheduleBinding binding;
    private NavController controller;
    private SchedulePresenter presenter;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firebaseFirestore;
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        initPresenterAndSendRequests(savedInstanceState);
        setDaysAdapter();
        if (mAuth.getCurrentUser() != null){
            binding.goodMorningTextView.append(mAuth.getCurrentUser().getDisplayName());
        }
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

    @Override
    public void onStart() {
        super.onStart();
        enableInteraction();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.presenter),presenter);
    }

    private void enableInteraction() {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    private void initPresenterAndSendRequests(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            presenter = savedInstanceState.getParcelable(getString(R.string.presenter));
            getDataFromPresenter(presenter.getSelectedDay().getDayNumber());

        }else{
            presenter = new SchedulePresenter(Repository.getInstance(RemoteDataSourceImpl.getInstance(), LocalDataSourceImp.getInstance(getContext())),this);
            presenter.getAllBreakfastMeals();
            presenter.getAllLaunchMeals();
            presenter.getAllDinnerMeals();
            presenter.getAllFavouriteMeals();
        }
    }





    private void popUpMenuListener(View view,Meal meal,int mealType)
    {

        if (((MainActivity)getActivity()).connectivityObserver.networkStatus()){
            Toast.makeText(requireContext(), "to take an action you need to be online", Toast.LENGTH_SHORT).show();
            return;
        }


        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.delete_item, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
         switch (mealType)
         {
             case breakfast:
             {

                 deleteItemFromCloudAndCache(getString(breakfast),meal,(mealToDelete)-> {
                     presenter.deleteBreakfastItem(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                     return null;

                 });
                 break;
             }

             case launch:
             {

                 deleteItemFromCloudAndCache(getString(launch),meal,(mealToDelete)-> {
                     presenter.deleteLaunchItem(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                     return null;

                 });
                 break;
             }

             case dinner:
             {
                 deleteItemFromCloudAndCache(getString(dinner),meal,(mealToDelete)-> {
                     presenter.deleteDinnerItem(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                     return null;

                 });
                 break;
             }

             case favourites:
             {

                 deleteItemFromCloudAndCache(getString(favourites),meal,(mealToDelete)-> {
                     presenter.deleteFavouritesItem(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                     return null;

                 });
                 break;
             }
         }
            return true;
        });

        popupMenu.show();

    }

    private void deleteItemFromCloudAndCache(String collectionName, Meal mealToDelete, Function<Meal,Void> function)
    {

        firebaseFirestore.collection(getString(R.string.users)).document(mAuth.getCurrentUser().getUid()).update(collectionName, FieldValue.arrayRemove(mealToDelete))
                .addOnSuccessListener(documentReference -> {
                    function.apply(mealToDelete);
                    Toast.makeText(requireContext(), getString(R.string.meal_is_deleted_successfully), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void syncCloudWithCache()
    {

        firebaseFirestore.collection(getString(R.string.users)).document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                   FirebaseMealsResponse cloudMeals = document.toObject(FirebaseMealsResponse.class);
                    presenter.syncDataWithCloud(cloudMeals.getBreakfasts(),cloudMeals.getLaunches()
                            ,cloudMeals.getDinners(),cloudMeals.getFavourites());
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }




    private void setRecyclerVisibility(View recyclerTitle ,View recycler, boolean visible)
    {

        if (visible){
            recyclerTitle.setVisibility(VISIBLE);
            recycler.setVisibility(VISIBLE);
        }else{
            recyclerTitle.setVisibility(GONE);
            recycler.setVisibility(GONE);

        }
    }

    private void setRecyclerView(RecyclerView recyclerView, Adapter adapter)
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

    private void getDataFromPresenter(String  numberOfDay)
    {
        List<Meal> breakfasts = presenter.getPresenterBreakfasts().stream().map(Breakfast::breakFastToMealMapper)
                .filter(meal ->
                        meal.getDay().equals(numberOfDay))
                .collect(Collectors.toList());


        List<Meal> launches = presenter.getPresenterLaunches().stream().map(Launch::launchToMealMapper)
                .filter(meal ->
                        meal.getDay().equals(numberOfDay))
                .collect(Collectors.toList());


        List<Meal> dinners = presenter.getPresenterDinners().stream().map(Dinner::dinnerToMealMapper)
                .filter(meal ->
                        meal.getDay().equals(numberOfDay)) .collect(Collectors.toList());


        List<Meal> favourites = presenter.getPresenterFavourites().stream().map(Favourite::favouriteToMealMapper).filter(meal ->
                        meal.getDay().equals(numberOfDay))
                .collect(Collectors.toList());


        setRecyclerVisibility(binding.breakFastTextView,binding.breakFastRecycler,!breakfasts.isEmpty());
        setRecyclerVisibility(binding.launchTextView,binding.launchRecycler,!launches.isEmpty());
        setRecyclerVisibility(binding.dinnerTextView,binding.dinnerRecycler,!dinners.isEmpty());
        setRecyclerVisibility(binding.favouritesTextView,binding.favouritesRecycler,!favourites.isEmpty());

        breakfastAdapter.setMeals(breakfasts
                , getContext(),breakfast,ScheduleFragment.this,ScheduleFragment.this);
        launchAdapter.setMeals(launches
                , getContext(),launch,ScheduleFragment.this,ScheduleFragment.this);

        dinnerAdapter.setMeals(dinners, getContext(),dinner,ScheduleFragment.this,
                ScheduleFragment.this);


        favouriteAdapter.setMeals(favourites
                , getContext(), R.string.favourites,ScheduleFragment.this,ScheduleFragment.this);

        daysAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDayClicked(Day day,int prevPosition) {

          day.setSelected(true);
          presenter.setSelectedDay(day);
          getDataFromPresenter(day.getDayNumber());
    }

    @Override
    public void onMealClicked(Meal meal, ImageView transitionView) {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(transitionView, transitionView.getTransitionName())
                .build();
        controller.navigate(ScheduleFragmentDirections.actionScheduleFragmentToMealDetailsFragment(transitionView.getTransitionName(),meal),extras);
    }


    @Override
    public void onMealLongClicked(View view, Meal meal, int mealType) {
        popUpMenuListener(view,meal,mealType);
    }

    @Override
    public void onBreakfastsSuccessCallback() {
        List<Meal> breakfasts = presenter.getPresenterBreakfasts().stream().map(Breakfast::breakFastToMealMapper)
                .filter(meal ->
                        meal.getDay().equals(presenter.getCurrentDay()))
                .collect(Collectors.toList());
            setRecyclerVisibility(binding.breakFastTextView,binding.breakFastRecycler,!breakfasts.isEmpty());
            breakfastAdapter.setMeals(breakfasts
                    , getContext(),breakfast,ScheduleFragment.this,ScheduleFragment.this);



    }

    @Override
    public void onLaunchesSuccessCallback() {

        List<Meal> launches = presenter.getPresenterLaunches().stream().map(Launch::launchToMealMapper)
                .filter(meal ->
                        meal.getDay().equals(presenter.getCurrentDay()))
                .collect(Collectors.toList());

            setRecyclerVisibility(binding.launchTextView,binding.launchRecycler,!launches.isEmpty());
            launchAdapter.setMeals(launches
                    , getContext(),launch,ScheduleFragment.this,ScheduleFragment.this);


    }

    @Override
    public void onDinnersSuccessCallback() {

        List<Meal> dinners = presenter.getPresenterDinners().stream().map(Dinner::dinnerToMealMapper)
                .filter(meal ->
                        meal.getDay().equals(presenter.getCurrentDay())) .collect(Collectors.toList());

            setRecyclerVisibility(binding.dinnerTextView,binding.dinnerRecycler,!dinners.isEmpty());
            dinnerAdapter.setMeals(dinners
                           , getContext(),dinner,ScheduleFragment.this,
                    ScheduleFragment.this);



    }

    @Override
    public void onFavouritesSuccessCallback() {
        List<Meal> favourites = presenter.getPresenterFavourites().stream().map(Favourite::favouriteToMealMapper).filter(meal ->
                        meal.getDay().equals(presenter.getCurrentDay()))
                .collect(Collectors.toList());
            setRecyclerVisibility(binding.favouritesTextView,binding.favouritesRecycler,!favourites.isEmpty());
            favouriteAdapter.setMeals(favourites
                    , getContext(), R.string.favourites,ScheduleFragment.this,ScheduleFragment.this);


    }
}