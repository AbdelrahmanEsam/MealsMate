package com.example.foodplanner.meals.detailsfragment.view;


import static com.example.foodplanner.R.string.breakfast;
import static com.example.foodplanner.R.string.dinner;
import static com.example.foodplanner.R.string.favourites;
import static com.example.foodplanner.R.string.launch;

import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.example.foodplanner.MainActivity;
import com.example.foodplanner.NavGraphDirections;
import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.local.LocalDataSourceImp;
import com.example.foodplanner.data.remote.RemoteDataSourceImpl;
import com.example.foodplanner.data.repository.Repository;
import com.example.foodplanner.databinding.FragmentMealDetailsBinding;
import com.example.foodplanner.meals.detailsfragment.presenter.MealDetailsPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class MealDetailsFragment extends Fragment {



    private FragmentMealDetailsBinding binding;
    private NavController controller;
    IngredientsAdapter ingredientsAdapter = new IngredientsAdapter();

    private FirebaseAuth mAuth ;

    private FirebaseFirestore fireStore;

    private MealDetailsPresenter presenter ;





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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        initPresenterAndSendRequests(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        initViews(presenter.getMealToAdd());
        binding.addToCacheFloatingButton.setOnClickListener(view1 -> {
            controller.navigate(NavGraphDirections.actionToAddDialogFragment(presenter.getMealToAdd()));
            addTypeObserver();
        });

    }

    private void initPresenterAndSendRequests(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {

            presenter = savedInstanceState.getParcelable(getString(R.string.presenter));

        }else{
            Meal meal =  getArguments().getParcelable(getString(R.string.meal));
            presenter = new MealDetailsPresenter(Repository.getInstance(RemoteDataSourceImpl.getInstance(), LocalDataSourceImp.getInstance(getContext())));
            presenter.setMealToAdd(meal);
        }
    }


    private void addTypeObserver()
    {
        NavBackStackEntry backStackEntry = controller.getCurrentBackStackEntry();

        backStackEntry.getSavedStateHandle().getLiveData(getString(R.string.type)).observe(getViewLifecycleOwner(), type -> {
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
                        Toast.makeText(requireContext(), R.string.meal_added_successfully, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }





    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).binding.bottomNavigationView.setVisibility(View.VISIBLE);

    }


    @Override
    public void onStop() {
        super.onStop();
        binding.youtubePlayerView.release();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.presenter),presenter);
    }

    private void initViews(Meal meal)
    {
        String transitionName = getArguments().getString(getString(R.string.transition_name));
        Log.d("transitionName",transitionName);
        binding.mealImage.setTransitionName(transitionName);
        Log.d("transitionName",binding.mealImage.getTransitionName());
        setUpYoutubeVideo(meal.getStrYoutube());

        Glide.with(requireContext())
                .load(meal.getStrMealThumb())
                .override(300, 200).downsample(DownsampleStrategy.CENTER_INSIDE)
                .into(binding.mealImage);

        binding.instructionsTextView.setText(meal.getStrInstructions());

        binding.mealNameTextView.setText(meal.getStrMeal());
        binding.areaTextView.setText(meal.getStrArea());

        setIngredientsRecycler(meal);

        binding.backImageView.setOnClickListener(view1 -> {
            controller.popBackStack();
        });

    }

    private void setUpYoutubeVideo(String url)
    {
        getLifecycle().addObserver(binding.youtubePlayerView);

        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(getYouTubeId(url), 0);
            }
        });



    }


    private String getYouTubeId (String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if(matcher.find()){
            return matcher.group();
        } else {
            return "error";
        }
    }







    private void setIngredientsRecycler(Meal meal)
    {
        ingredientsAdapter.setData(getIngredients(meal),getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.ingredientsRecyclerView.setLayoutManager(layoutManager);
        binding.ingredientsRecyclerView.setAdapter(ingredientsAdapter);

    }

    private List<String> getIngredients(Meal meal)
    {
        List<String> ingredients = Arrays.stream(meal.getClass().getMethods()).filter(method -> method.getName().toLowerCase().contains(getString(R.string.ingredient))).filter(method -> method.getName().toLowerCase().contains("get")).map(method -> {
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