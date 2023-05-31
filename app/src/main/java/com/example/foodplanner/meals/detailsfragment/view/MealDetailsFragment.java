package com.example.foodplanner.meals.detailsfragment.view;


import static com.example.foodplanner.R.string.breakfast;
import static com.example.foodplanner.R.string.dinner;
import static com.example.foodplanner.R.string.favourites;
import static com.example.foodplanner.R.string.launch;
import static com.example.foodplanner.R.string.you_need_to_be_authenticated_to_do_this_action_please_login;

import android.os.Bundle;
import android.os.Handler;
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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


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


            if (mAuth.getCurrentUser() !=null) {
            controller.navigate(NavGraphDirections.actionToDatePickerFragment(presenter.getMealToAdd()));
            }else{
                Toast.makeText(getContext(), getString(you_need_to_be_authenticated_to_do_this_action_please_login), Toast.LENGTH_SHORT).show();
            }

        });
        addTypeObserver();
        datePickerResultObserver();


    }

    @Override
    public void onStart() {
        super.onStart();
        setYoutubeVideoVisibility();
    }

    private void setYoutubeVideoVisibility()
    {
     if (((MainActivity)requireActivity()).connectivityObserver.networkStatus()){

        binding.youtubePlayerView.setVisibility(View.GONE);
     }
    }

    private void initPresenterAndSendRequests(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {

            Log.d("presenter","not null");
            presenter = savedInstanceState.getParcelable(getString(R.string.presenter));
            if (presenter == null){

            }

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
                            presenter.insertMealToBreakfast(meal).subscribeOn(Schedulers.io())
                                    .subscribeOn(AndroidSchedulers.mainThread()).subscribe();
                            return null;
                        });
                        break;
                    }

                    case launch: {
                        addRecordToFirebaseAndRoom(getString(launch),presenter.getMealToAdd(),(meal)-> {
                            presenter.insertMealToLaunch(meal).subscribeOn(Schedulers.io())
                                    .subscribeOn(AndroidSchedulers.mainThread()).subscribe();
                            return null;
                        });
                        break;
                    }

                    case dinner: {
                        addRecordToFirebaseAndRoom(getString(dinner),presenter.getMealToAdd(),(meal)-> {
                            presenter.insertMealToDinner(meal).subscribeOn(Schedulers.io())
                                    .subscribeOn(AndroidSchedulers.mainThread()).subscribe();

                            return null;
                        });
                        break;

                    }

                    case favourites:{
                        addRecordToFirebaseAndRoom(getString(favourites),presenter.getMealToAdd(),(meal)-> {

                            presenter.insertMealToFavourite(meal).subscribeOn(Schedulers.io())
                                    .subscribeOn(AndroidSchedulers.mainThread()).subscribe();

                            return null;

                        });
                        break;

                    }
                }
                controller.getPreviousBackStackEntry().getSavedStateHandle().set(getString(R.string.type),null);

            }

        });



    }


    private void datePickerResultObserver()
    {
        NavBackStackEntry backStackEntry = controller.getCurrentBackStackEntry();
        backStackEntry.getSavedStateHandle().getLiveData(getString(R.string.date)).observe(getViewLifecycleOwner(), type -> {
            if (type != null) {
                Log.d("dateListener", type.toString());

                new Handler().postDelayed(() -> {
                    presenter.getMealToAdd().setDay(type.toString());
                    controller.navigate(NavGraphDirections.actionToAddDialogFragment(presenter.getMealToAdd()));
                    controller.getPreviousBackStackEntry().getSavedStateHandle().set(getString(R.string.date), null);
                }, 50);

            }

        });
    }

    private void addRecordToFirebaseAndRoom(String collectionName, Meal mealToAdd, Function<Meal,Void> function)
    {

            fireStore.collection(getString(R.string.users)).document(mAuth.getCurrentUser().getUid()).update(collectionName, FieldValue.arrayUnion(mealToAdd))
                    .addOnSuccessListener(documentReference -> {
                        if (mealToAdd != null) {
                            function.apply(mealToAdd);
                            Toast.makeText(requireContext(), R.string.meal_added_successfully, Toast.LENGTH_SHORT).show();
                        } else {

                            Log.d("null Meal", "null");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

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
        binding.mealImage.setTransitionName(transitionName);
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
                youTubePlayer.loadVideo(presenter.getYouTubeId(url), 0);
            }
        });



    }


    private void setIngredientsRecycler(Meal meal)
    {
        ingredientsAdapter.setData(presenter.getIngredients(meal),getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.ingredientsRecyclerView.setLayoutManager(layoutManager);
        binding.ingredientsRecyclerView.setAdapter(ingredientsAdapter);

    }




}