package com.example.foodplanner.meals.detailsfragment.view;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.example.foodplanner.MainActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.databinding.FragmentMealDetailsBinding;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MealDetailsFragment extends Fragment {



    private FragmentMealDetailsBinding binding;
    private NavController controller;
    IngredientsAdapter ingredientsAdapter = new IngredientsAdapter();



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
        binding = FragmentMealDetailsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
       Meal meal =  getArguments().getParcelable(getString(R.string.meal));
        initViews(meal);









    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).binding.bottomNavigationView.setVisibility(View.VISIBLE);

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initViews(Meal meal)
    {
        String transitionName = getArguments().getString(getString(R.string.transition_name));
        binding.mealImage.setTransitionName(transitionName);

        setUpYoutubeVideo(meal.getStrYoutube());

        Glide.with(requireContext())
                .load(meal.getStrMealThumb())
                .override(300, 200).downsample(DownsampleStrategy.CENTER_INSIDE)
                .into(binding.mealImage);

        binding.instructionsTextView.setText(meal.getStrInstructions());

        setIngredientsRecycler(meal);

        binding.backImageView.setOnClickListener(view1 -> {
            controller.popBackStack();
        });

    }

    private void setUpYoutubeVideo(String url)
    {
        Log.d("youtube",url);

        binding.youtubePlayerView.setWebViewClient(new WebViewClient());
        binding.youtubePlayerView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.youtubePlayerView.getSettings().setJavaScriptEnabled(true);
        binding.youtubePlayerView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.youtubePlayerView.setWebChromeClient(new WebChromeClient());
        binding.youtubePlayerView.loadUrl(url);


    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setIngredientsRecycler(Meal meal)
    {
        ingredientsAdapter.setIngredients(getIngredients(meal),getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.ingredientsRecyclerView.setLayoutManager(layoutManager);
        binding.ingredientsRecyclerView.setAdapter(ingredientsAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<String> getIngredients(Meal meal)
    {


        List<String> ingredients = Arrays.stream(meal.getClass().getMethods()).filter(method -> method.getName().toLowerCase().contains(getString(R.string.ingredient))).map(method -> {


            String ingredientName   = null;
            try {
                ingredientName = (String) method.invoke(meal);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return ingredientName;
        }).collect(Collectors.toList());
        ingredients.removeIf(String::isEmpty);
        return ingredients;
    }
}