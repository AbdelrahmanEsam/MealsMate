package com.example.foodplanner.boarding.presentation.categories.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.databinding.FragmentCategoriesBinding;
import com.example.utils.CardsAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;


public class CategoriesFragment extends Fragment {

    private FragmentCategoriesBinding binding;
    private NavController controller ;
    private List<String> routines = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        routines.add("vegan");
        routines.add("vegeterian");
        routines.add("pork");
        CardsAdapter adapter  = new CardsAdapter();
        adapter.setCards(routines,getContext());
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);


        binding.routinesRecycler.setLayoutManager(layoutManager);
        binding.routinesRecycler.setAdapter(adapter);

        binding.forwardImageView.setOnClickListener(view1 -> {

            //List<String> selectedRoutines = adapter.getSelectedRoutines();
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(binding.cardProgress, "orangeProgress")
                    .build();
            controller.navigate(CategoriesFragmentDirections.actionCategoriesFragmentToFavouritesFragment()
                    ,extras);



        });


    }
}