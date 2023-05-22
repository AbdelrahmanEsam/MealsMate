package com.example.foodplanner.boarding.presentation.favourites.view;

import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.foodplanner.databinding.FragmentChooseFavouritesBinding;

import java.util.ArrayList;
import java.util.List;


public class SelectFavouritesFragment extends Fragment {


    private FragmentChooseFavouritesBinding binding;
    private NavController controller ;
    private List<String> favourites = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move);
        setSharedElementEnterTransition(transition);
        setSharedElementReturnTransition(transition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChooseFavouritesBinding.inflate(inflater, container, false);
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
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(binding.cardProgress, "orangeProgress")
                    .build();
            controller.navigate(SelectFavouritesFragmentDirections.actionFavouritesFragmentToAllergiesFragment(),extras);
        });
    }
}