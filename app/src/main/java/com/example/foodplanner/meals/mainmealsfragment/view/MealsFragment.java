package com.example.foodplanner.meals.mainmealsfragment.view;

import static com.example.foodplanner.R.*;
import static com.example.foodplanner.R.id.*;
import static com.example.foodplanner.R.id.byCategory;
import static com.example.foodplanner.R.id.byName;
import static com.example.foodplanner.R.string.breakfast;
import static com.example.foodplanner.R.string.dinner;
import static com.example.foodplanner.R.string.favourites;
import static com.example.foodplanner.R.string.launch;
import static com.example.foodplanner.R.string.meal;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.example.foodplanner.MainActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.data.local.LocalDataSourceImp;
import com.example.foodplanner.data.remote.RemoteDataSourceImpl;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.repository.Repository;
import com.example.foodplanner.databinding.FragmentMealsBinding;
import com.example.foodplanner.meals.mainmealsfragment.presenter.MealsPresenter;
import com.example.utils.CustomFlexLayoutManager;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MealsFragment extends Fragment implements OnMealClickListener,OnMealAddClicked, MealsFragmentViewInterface {


    private FragmentMealsBinding binding;
    private NavController controller;
    private final MealsAdapter adapter  = new MealsAdapter();
    private MealsPresenter presenter;

    private FirebaseAuth mAuth ;

    private FirebaseFirestore fireStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMealsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        controller = Navigation.findNavController(view);
        presenter = new MealsPresenter(Repository.getInstance(RemoteDataSourceImpl.getInstance(), LocalDataSourceImp.getInstance(getContext())),this);
        presenter.getAllMeals();
        presenter.mealOfTheDayRequest();
        disableInteraction();
        binding.goodMorningTextView.append(mAuth.getCurrentUser().getDisplayName());
        addTypeObserver();

        binding.mealOfTheDayCardView.setOnClickListener(view1 -> {
           navigateToDetails(presenter.getMealOfTheDay(),binding.mealImage);
        });



        binding.searchImageView.setOnClickListener(view12 -> {
            popUpMenuListener();
        });

        setRecyclerView();

    }



    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }



    private void addTypeObserver()
    {
        NavBackStackEntry backStackEntry = controller.getCurrentBackStackEntry();
        backStackEntry.getSavedStateHandle().getLiveData("type").observe(getViewLifecycleOwner(),type -> {
            if (type != null) {
                switch ((Integer) type) {
                    case breakfast: {
                        addRecordToFirebaseAndRoom(getString(breakfast),presenter.getMealToAdd(),(meal)-> {
                            presenter.insertMealToBreakfast(meal);
                            return null;

                        });
                        break;
                    }

                    case launch: {
                        addRecordToFirebaseAndRoom(getString(launch),presenter.getMealToAdd(),(meal)-> {
                            presenter.insertMealToLaunch(meal);
                            return null;
                        });
                        break;
                    }

                    case dinner: {
                        addRecordToFirebaseAndRoom(getString(dinner),presenter.getMealToAdd(),(meal)-> {
                            presenter.insertMealToDinner(meal);
                            return null;
                        });
                        break;

                    }

                    case favourites:{
                            addRecordToFirebaseAndRoom(getString(favourites),presenter.getMealToAdd(),(meal)-> {

                                presenter.insertMealToFavourite(meal);
                                return null;

                            });
                        break;

                    }
                }
            }

        });



    }


    private void addRecordToFirebaseAndRoom(String collectionName, Meal mealToAdd, Function<Meal,Void> function)
    {

        fireStore.collection(getString(R.string.users)).document(mAuth.getCurrentUser().getUid()).update(collectionName, FieldValue.arrayUnion(mealToAdd))
                .addOnSuccessListener(documentReference -> {
                    if (mealToAdd != null)
                    {
                        function.apply(mealToAdd);
                        Toast.makeText(requireContext(), string.meal_added_successfully, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void popUpMenuListener()
    {

        PopupMenu popupMenu = new PopupMenu(requireContext(), binding.searchImageView);


        popupMenu.getMenuInflater().inflate(menu.search_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId())
            {

                case  byName:{
                    binding.searchInputLayout.getEditText().setText(string.type_in_your_favourite_dish);
                    break;
                }

                case  byCategory:{
                    binding.searchInputLayout.getEditText().setText(string.category_placeholder);


                    break;
                }

                case  byCountry:{
                    binding.searchInputLayout.getEditText().setText(string.nationality_placeholder);

                    break;
                }


                case  byIngredient:{
                    binding.searchInputLayout.getEditText().setText(string.ingredient_placeholder);
                    break;
                }


            }
            return true;
        });

        popupMenu.show();

    }


    private void enableInteraction()
    {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }


    private void disableInteraction()
    {
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }


    private void setRecyclerView()
    {
        FlexboxLayoutManager layoutManager = new CustomFlexLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);

        binding.mealsRecycler.setLayoutManager(layoutManager);
        binding.mealsRecycler.setAdapter(adapter);
        binding.mealsRecycler.setNestedScrollingEnabled(false);

    }

    private void navigateToDetails(Meal meal , View transitionView)
    {


        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(transitionView, transitionView.getTransitionName())
                .build();
        controller.navigate(MealsFragmentDirections.actionMealsFragmentToMealDetailsFragment(transitionView.getTransitionName(),meal),extras);
    }

    @Override
    public void onMealClicked(Meal meal, ImageView transitionView) {

        navigateToDetails(meal,transitionView);
    }


    @Override
    public void onMealAddClicked(Meal meal) {
        presenter.setMealToAdd(meal);
        controller.navigate(MealsFragmentDirections.actionMealsFragmentToAddDialogFragment(meal));
    }

    @Override
    public void onResultSuccessAllMealsCallback(List<Meal> meals) {
        enableInteraction();
        adapter.setMeals(meals,getContext(),MealsFragment.this,MealsFragment.this);
    }

    @Override
    public void onResultFailureAllMealsCallback(String error) {

    }

    @Override
    public void onResultSuccessOneMealsCallback(Meal meal) {
        enableInteraction();
        binding.aboutTextView.setText(meal.getStrMeal());
        Glide.with(requireContext())
                .load(meal.getStrMealThumb())
                .override(300, 200).downsample(DownsampleStrategy.CENTER_INSIDE)
                .into(binding.mealImage);
    }

    @Override
    public void onResultFailureOneMealCallback(String error) {

    }
}