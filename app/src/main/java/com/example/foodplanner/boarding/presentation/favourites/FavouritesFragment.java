package com.example.foodplanner.boarding.presentation.favourites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.boarding.presentation.categories.RoutinesAdapter;
import com.example.foodplanner.databinding.FragmentCategoriesBinding;
import com.example.foodplanner.databinding.FragmentFavouritesBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;


public class FavouritesFragment extends Fragment {


    private FragmentFavouritesBinding binding;
    private NavController controller ;
    private List<String> favourites = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        favourites.add("vegan");
        favourites.add("vegeterian");
        favourites.add("pork");
        favourites.add("pork");
        favourites.add("pork");
        favourites.add("pork");
        favourites.add("pork");
        FavouritesAdapter adapter  = new FavouritesAdapter();
        adapter.setFavourites(favourites,getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);




        binding.favouritesRecycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.favouritesRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int viewWidth = binding.favouritesRecycler.getMeasuredWidth();
                float cardViewWidth = getContext().getResources().getDimension(com.intuit.sdp.R.dimen._100sdp);
                int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                gridLayoutManager.setSpanCount(newSpanCount);
                gridLayoutManager.requestLayout();
            }
        });
        binding.favouritesRecycler.setLayoutManager(gridLayoutManager);
        binding.favouritesRecycler.setAdapter(adapter);
        binding.forwardImageView.setOnClickListener(view1 -> {
            List<String> selectedFavourites = adapter.getSelectedFavourites();

            controller.navigate(FavouritesFragmentDirections.actionFavouritesFragmentToAllergiesFragment());
        });
    }
}