package com.example.foodplanner.meals.mainmealsfragment.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.databinding.FragmentAddDialogBinding;
import com.example.foodplanner.databinding.FragmentMealsBinding;


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
        //controller = Navigation.findNavController(view);
        initViews();

    }

    private void initViews()
    {

        binding.addToLaunch.mealNameTextView.setText(R.string.launch);
        binding.addToDinner.mealNameTextView.setText(R.string.dinner);
        binding.addToFavourites.mealNameTextView.setText(R.string.favourites);



    }
}