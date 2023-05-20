package com.example.foodplanner.boarding.presentation.allergies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.boarding.presentation.categories.RoutinesAdapter;
import com.example.foodplanner.databinding.FragmentAllergiesBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

public class AllergiesFragment extends Fragment {

    private FragmentAllergiesBinding binding;
    private NavController controller ;
    private List<String> allergies = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllergiesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        allergies.add("egg");
        allergies.add("fish");
        allergies.add("soy");
        AllergiesAdapter adapter  = new AllergiesAdapter();
        adapter.setAllergies(allergies,getContext());
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);


        binding.allergiesRecycler.setLayoutManager(layoutManager);
        binding.allergiesRecycler.setAdapter(adapter);

    }
}