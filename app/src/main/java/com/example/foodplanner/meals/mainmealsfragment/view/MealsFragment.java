package com.example.foodplanner.meals.mainmealsfragment.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

public class MealsFragment extends Fragment implements OnMealClickListener,OnMealAddClicked, MealsFragmentViewInterface {


    private FragmentMealsBinding binding;
    private NavController controller;
    private MealsAdapter adapter  = new MealsAdapter();
    private MealsPresenter presenter;
    private Meal mealToAdd;

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
        controller = Navigation.findNavController(view);
        presenter = new MealsPresenter(Repository.getInstance(RemoteDataSourceImpl.getInstance(), LocalDataSourceImp.getInstance(getContext())),this);
        presenter.getAllMeals();
        presenter.getMealOfTheDay();
        addTypeObserver();

        binding.mealOfTheDayBarrier.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "yes", Toast.LENGTH_SHORT).show();
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
                    case R.string.breakfast: {
                        presenter.insertMealToBreakfast(mealToAdd);
                        break;
                    }

                    case R.string.launch: {
                        presenter.insertMealToLaunch(mealToAdd);
                        break;

                    }

                    case R.string.dinner: {
                        presenter.insertMealToDinner(mealToAdd);
                        break;

                    }

                    case R.string.favourites:{
                        presenter.insertMealToFavourite(mealToAdd);
                        break;

                    }
                }
            }

        });



    }


    private void popUpMenuListener()
    {

        PopupMenu popupMenu = new PopupMenu(requireContext(), binding.searchImageView);


        popupMenu.getMenuInflater().inflate(R.menu.search_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId())
            {

                case  R.id.byName:{


                    binding.searchInputLayout.getEditText().setText(R.string.type_in_your_favourite_dish);
                    break;
                }

                case  R.id.byCategory:{
                    binding.searchInputLayout.getEditText().setText(R.string.category_placeholder);


                    break;
                }

                case  R.id.byCountry:{
                    binding.searchInputLayout.getEditText().setText(R.string.nationality_placeholder);

                    break;
                }


                case  R.id.byIngredient:{
                    binding.searchInputLayout.getEditText().setText(R.string.ingredient_placeholder);
                    break;
                }


            }
            return true;
        });

        popupMenu.show();

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

    @Override
    public void onMealClicked(Meal meal, ImageView transitionView) {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(transitionView, transitionView.getTransitionName())
                .build();
        controller.navigate(MealsFragmentDirections.actionMealsFragmentToMealDetailsFragment(transitionView.getTransitionName(),meal),extras);
    }

    @Override
    public void onMealAddClicked(Meal meal) {
        mealToAdd = meal;
        controller.navigate(MealsFragmentDirections.actionMealsFragmentToAddDialogFragment(meal));
    }

    @Override
    public void onResultSuccessAllMealsCallback(List<Meal> meals) {
        adapter.setMeals(meals,getContext(),MealsFragment.this,MealsFragment.this);
    }

    @Override
    public void onResultFailureAllMealsCallback(String error) {

    }

    @Override
    public void onResultSuccessOneMealsCallback(Meal meal) {

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