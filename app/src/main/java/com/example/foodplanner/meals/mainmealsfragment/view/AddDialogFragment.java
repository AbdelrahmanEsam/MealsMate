package com.example.foodplanner.meals.mainmealsfragment.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.databinding.FragmentAddDialogBinding;


public class AddDialogFragment extends DialogFragment {

    private FragmentAddDialogBinding binding;
    private NavController controller;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.MyDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = NavHostFragment.findNavController(this);
        Meal meal =  getArguments().getParcelable(getString(R.string.meal));
        initViews();
        clickListeners(meal);


    }

    private void clickListeners(Meal meal)
    {
        Log.d("addDialog",meal.getStrMeal());
        binding.addToBreakfast.getRoot().setOnClickListener(view1 -> {

            controller.getPreviousBackStackEntry().getSavedStateHandle().set("type",R.string.breakfast);
            dismiss();

        });


        binding.addToLaunch.getRoot().setOnClickListener(view1 -> {
            controller.getPreviousBackStackEntry().getSavedStateHandle().set("type",R.string.launch);
            dismiss();
        });


        binding.addToDinner.getRoot().setOnClickListener(view1 -> {
            controller.getPreviousBackStackEntry().getSavedStateHandle().set("type",R.string.dinner);
            dismiss();
        });

        binding.addToFavourites.getRoot().setOnClickListener(view1 -> {
            controller.getPreviousBackStackEntry().getSavedStateHandle().set("type",R.string.favourites);
            dismiss();
        });
    }

    private void initViews()
    {

        binding.addToLaunch.mealNameTextView.setText(R.string.launch);
        binding.addToDinner.mealNameTextView.setText(R.string.dinner);
        binding.addToFavourites.mealNameTextView.setText(R.string.favourites);



    }
}