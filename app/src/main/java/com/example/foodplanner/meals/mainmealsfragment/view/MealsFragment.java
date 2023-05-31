package com.example.foodplanner.meals.mainmealsfragment.view;

import static com.example.foodplanner.R.*;
import static com.example.foodplanner.R.string.breakfast;
import static com.example.foodplanner.R.string.dinner;
import static com.example.foodplanner.R.string.favourites;
import static com.example.foodplanner.R.string.launch;
import static com.example.foodplanner.R.string.you_need_to_be_authenticated_to_do_this_action_please_login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.example.foodplanner.MainActivity;
import com.example.foodplanner.NavGraphDirections;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsFragment extends Fragment implements MealsFragmentViewInterface{


    private FragmentMealsBinding binding;
    private NavController controller;
    private  MealsAdapter adapter;
    private MealsPresenter presenter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;


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
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        controller = Navigation.findNavController(view);
        adapter = new MealsAdapter();
        setRecyclerView();
        initState(savedInstanceState);



        if (mAuth.getCurrentUser() != null) {
            binding.goodMorningTextView.append(mAuth.getCurrentUser().getDisplayName());
        }

        binding.mealOfTheDayCardView.setOnClickListener(view1 -> {
            navigateToDetails(presenter.getMealOfTheDay(), binding.mealImage);
        });


        binding.logoutImageView.setOnClickListener(view1 -> {
            mAuth.signOut();
            presenter.clearAllTables();
            ((MainActivity)requireActivity()).binding.bottomNavigationView.setVisibility(View.GONE);
            controller.navigate(NavGraphDirections.actionToLoginFragment());
        });

        addTypeObserver();

        datePickerResultObserver();
        searchObserverAndObservable();

    }


    private void initState(Bundle savedInstanceState)
    {
        if (savedInstanceState != null) {
            presenter = savedInstanceState.getParcelable(getString(string.presenter));
            if (presenter != null) {
                presenter.setViewInterface(this);
                adapter.setMeals(presenter.getAllMeals(), getContext(), MealsFragment.this);
                setMealOfTheDay();
                binding.searchInputLayout.getEditText().setText(presenter.getSearchPrefix());
            } else {


                initPresenterAndSendRequests();

            }

        } else {

            initPresenterAndSendRequests();

        }
    }

    private void initViews()
    {
        if (mAuth.getCurrentUser() == null){
            binding.logoutImageView.setVisibility(View.GONE);
           ((MainActivity)requireActivity()).binding.bottomNavigationView.setVisibility(View.VISIBLE);
          ((MainActivity)requireActivity()).binding.bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(string.presenter), presenter);
    }

    @Override
    public void onStop() {
        super.onStop();
        closeKeyboard();
    }

    @SuppressLint("CheckResult")
    private void searchObserverAndObservable() {


        Observable<String> textChangeObservable = Observable.create(emitter ->
                binding.searchInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                         emitter.onNext(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                }));



        textChangeObservable
                .debounce(1000, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread()).subscribe(text -> {
               presenter.setSearchPrefix(text);

               presenter.searchByNameMealRequest(text);
           });





    }




    private void initPresenterAndSendRequests() {
        presenter = new MealsPresenter(Repository.getInstance(RemoteDataSourceImpl.getInstance(), LocalDataSourceImp.getInstance(getContext())), this);
        presenter.searchByNameMealRequest("");
        presenter.mealOfTheDayRequest();
         disableInteraction();

    }

    private void addTypeObserver() {
        NavBackStackEntry backStackEntry = controller.getCurrentBackStackEntry();

        backStackEntry.getSavedStateHandle().getLiveData(getString(R.string.type)).observe(getViewLifecycleOwner(), type -> {
            if (type != null) {
                switch ((Integer) type) {
                    case breakfast: {
                        addRecordToFirebaseAndRoom(getString(breakfast), presenter.getMealToAdd(), (meal) -> {
                            presenter.insertMealToBreakfast(meal).subscribeOn(Schedulers.io())
                                    .subscribeOn(AndroidSchedulers.mainThread()).subscribe();
                            return null;
                        });
                        break;
                    }

                    case launch: {
                        addRecordToFirebaseAndRoom(getString(launch), presenter.getMealToAdd(), (meal) -> {
                            presenter.insertMealToLaunch(meal).subscribeOn(Schedulers.io())
                                    .subscribeOn(AndroidSchedulers.mainThread()).subscribe();
                            return null;
                        });
                        break;
                    }

                    case dinner: {
                        addRecordToFirebaseAndRoom(getString(dinner), presenter.getMealToAdd(), (meal) -> {
                            presenter.insertMealToDinner(meal).subscribeOn(Schedulers.io())
                                    .subscribeOn(AndroidSchedulers.mainThread()).subscribe();
                            return null;
                        });
                        break;

                    }

                    case favourites: {
                        addRecordToFirebaseAndRoom(getString(favourites), presenter.getMealToAdd(), (meal) -> {

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

    private void addRecordToFirebaseAndRoom(String collectionName, Meal mealToAdd, Function<Meal, Void> function) {


            fireStore.collection(getString(R.string.users)).document(mAuth.getCurrentUser().getUid()).update(collectionName, FieldValue.arrayUnion(mealToAdd))
                    .addOnSuccessListener(documentReference -> {
                        if (mealToAdd != null) {
                            function.apply(mealToAdd);
                            Toast.makeText(requireContext(), string.meal_added_successfully, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

    }
    private void datePickerResultObserver()
    {
        NavBackStackEntry backStackEntry = controller.getCurrentBackStackEntry();
        backStackEntry.getSavedStateHandle().getLiveData(getString(string.date)).observe(getViewLifecycleOwner(), type -> {
            if (type != null) {
                Log.d("dateListener", type.toString());

                new Handler().postDelayed(() -> {
                    presenter.getMealToAdd().setDay(type.toString());
                    controller.navigate(NavGraphDirections.actionToAddDialogFragment(presenter.getMealToAdd()));
                    controller.getPreviousBackStackEntry().getSavedStateHandle().set(getString(string.date), null);
                }, 50);

            }

                });
    }







    private void enableInteraction() {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }


    private void disableInteraction() {
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager.isAcceptingText()) {

            inputManager.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void setRecyclerView() {
        FlexboxLayoutManager layoutManager = new CustomFlexLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        binding.mealsRecycler.setLayoutManager(layoutManager);
        binding.mealsRecycler.setAdapter(adapter);
        binding.mealsRecycler.setNestedScrollingEnabled(false);

    }

    private void navigateToDetails(Meal meal, View transitionView) {


        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(transitionView, transitionView.getTransitionName())
                .build();
        controller.navigate(NavGraphDirections.actionToMealDetailsFragment(transitionView.getTransitionName(), meal), extras);
    }

    private void setMealOfTheDay() {
        Meal meal = presenter.getMealOfTheDay();



            if (meal != null && isAdded()) {
                binding.aboutTextView.setText(meal.getStrMeal());
                Glide.with(requireContext())
                        .load(meal.getStrMealThumb())
                        .override(300, 200).downsample(DownsampleStrategy.CENTER_INSIDE)
                        .into(binding.mealImage);
            }


    }

    @Override
    public void onMealClicked(Meal meal, ImageView transitionView) {

        navigateToDetails(meal, transitionView);

    }


    @Override
    public void onMealAddClicked(Meal meal) {
        presenter.setMealToAdd(meal);
        if (mAuth.getCurrentUser() != null) {
            controller.navigate(NavGraphDirections.actionToDatePickerFragment(meal));
        }else{
            Toast.makeText(getContext(), getString(you_need_to_be_authenticated_to_do_this_action_please_login), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResultSuccessOneMealsCallback() {
        setMealOfTheDay();
    }

    @Override
    public void onResultFailureOneMealCallback(String error) {

        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchSuccessResult() {
        closeKeyboard();
        enableInteraction();
        adapter.setMeals(presenter.getAllMeals(), getContext(), MealsFragment.this);
    }




}