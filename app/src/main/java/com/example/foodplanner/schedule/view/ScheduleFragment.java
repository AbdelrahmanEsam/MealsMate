package com.example.foodplanner.schedule.view;

import static com.example.foodplanner.R.string.breakfast;
import static com.example.foodplanner.R.string.dinner;
import static com.example.foodplanner.R.string.favourites;
import static com.example.foodplanner.R.string.launch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ScheduleFragment extends Fragment implements OnDayListener, OnMealClickListener,OnMealLongClickedListener{

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



    private void popUpMenuListener(View view,Meal meal,int mealType)
    {

        PopupMenu popupMenu = new PopupMenu(requireContext(), view);



        popupMenu.getMenuInflater().inflate(R.menu.delete_item, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
         switch (mealType)
         {
             case breakfast:
             {

                 deleteItemFromCloudAndCache(getString(breakfast),meal,(mealToDelete)-> {
                     presenter.deleteBreakfastItem(meal);
                     return null;

                 });
                 break;
             }

             case launch:
             {

                 deleteItemFromCloudAndCache(getString(launch),meal,(mealToDelete)-> {
                     presenter.deleteLaunchItem(meal);
                     return null;

                 });
                 break;
             }

             case dinner:
             {
                 deleteItemFromCloudAndCache(getString(dinner),meal,(mealToDelete)-> {
                     presenter.deleteDinnerItem(meal);
                     return null;

                 });
                 break;
             }

             case favourites:
             {

                 deleteItemFromCloudAndCache(getString(favourites),meal,(mealToDelete)-> {
                     presenter.deleteFavouritesItem(meal);
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


        firebaseFirestore.collection(getString(R.string.users)).document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                       FirebaseMealsResponse cloudMeals = document.toObject(FirebaseMealsResponse.class);
                        presenter.syncDataWithCloud(cloudMeals.getBreakfasts(),cloudMeals.getLaunches()
                                ,cloudMeals.getDinners(),cloudMeals.getFavourites());





                    }
                }else{
                    Log.d("breakfast","hhhh");
                }
            }
        }).addOnFailureListener(e -> {
            Log.d("breakfast",e.getMessage());
        });





    }


    private void breakfastMealsObservers()
    {
        presenter.getAllBreakfastMeals().observe(getViewLifecycleOwner(),breakfasts -> {
            if (!breakfasts.isEmpty())
            {
                binding.breakFastTextView.setVisibility(View.VISIBLE);
                binding.breakFastRecycler.setVisibility(View.VISIBLE);

            }else
            {
                binding.breakFastTextView.setVisibility(View.GONE);
                binding.breakFastRecycler.setVisibility(View.GONE);
            }
            breakfastAdapter.setMeals(breakfasts.stream().map(Breakfast::breakFastToMealMapper).collect(Collectors.toList()), getContext(),breakfast,this,this);
        });

    }


    private void launchMealsObservers()
    {
        presenter.getAllLaunchMeals().observe(getViewLifecycleOwner(),launches -> {

            if (!launches.isEmpty())
            {
                binding.launchTextView.setVisibility(View.VISIBLE);
                binding.launchRecycler.setVisibility(View.VISIBLE);
            }else {
                binding.launchTextView.setVisibility(View.GONE);
                binding.launchRecycler.setVisibility(View.GONE);
            }
            launchAdapter.setMeals(launches.stream().map(Launch::launchToMealMapper).collect(Collectors.toList()), getContext(),launch,this,this);
        });

    }


    private void dinnerMealsObservers()
    {
        presenter.getAllDinnerMeals().observe(getViewLifecycleOwner(),dinners -> {
            if (!dinners.isEmpty())
            {
                binding.dinnerTextView.setVisibility(View.VISIBLE);
                binding.dinnerRecycler.setVisibility(View.VISIBLE);
            }else {
                binding.dinnerTextView.setVisibility(View.GONE);
                binding.dinnerRecycler.setVisibility(View.GONE);
            }
            dinnerAdapter.setMeals(dinners.stream().map(Dinner::dinnerToMealMapper).collect(Collectors.toList()), getContext(),dinner,this,this);

        });

    }


    private void favouritesMealsObservers()
    {
        presenter.getAllFavouriteMeals().observe(getViewLifecycleOwner(),favourites -> {

            if (!favourites.isEmpty())
            {
                binding.favouritesTextView.setVisibility(View.VISIBLE);
                binding.favouritesRecycler.setVisibility(View.VISIBLE);
            }else {
                binding.favouritesTextView.setVisibility(View.GONE);
                binding.favouritesRecycler.setVisibility(View.GONE);
            }
            favouriteAdapter.setMeals(favourites.stream().map(Favourite::favouriteToMealMapper).collect(Collectors.toList()), getContext(), R.string.favourites,this,this);

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


    @Override
    public void onMealLongClicked(View view, Meal meal, int mealType) {
        popUpMenuListener(view,meal,mealType);
    }
}