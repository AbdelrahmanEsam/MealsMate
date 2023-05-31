package com.example.foodplanner.meals.search.searchresults.view;

import static com.example.foodplanner.R.string.*;
import static com.example.foodplanner.R.string.country;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodplanner.NavGraphDirections;
import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.search.FilterMeal;
import com.example.foodplanner.data.local.LocalDataSource;
import com.example.foodplanner.data.local.LocalDataSourceImp;
import com.example.foodplanner.data.remote.RemoteDataSourceImpl;
import com.example.foodplanner.data.repository.Repository;
import com.example.foodplanner.databinding.FragmentSearchResultsBinding;
import com.example.foodplanner.meals.mainmealsfragment.view.MealsAdapter;
import com.example.foodplanner.meals.search.searchresults.presenter.SearchResultsPresenter;
import com.example.utils.CustomFlexLayoutManager;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.function.Function;


public class SearchResultsFragment extends Fragment
        implements  SearchResultsViewInterface,OnFilterMealClickListener,OnFilterMealAddClickedListener {


    private SearchResultsPresenter presenter ;

    private FragmentSearchResultsBinding binding;

    private final FilterMealsAdapter mealsAdapter = new FilterMealsAdapter();

    private View transitionView;


    private NavController controller;

    private FirebaseAuth mAuth ;
    private FirebaseFirestore fireStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        controller = Navigation.findNavController(view);

        initPresenterAndSendRequests(savedInstanceState);
        addTypeObserver();
        datePickerResultObserver();
        setRecyclerView();
    }

    private void initPresenterAndSendRequests(Bundle savedInstanceState)
    {
        String searchWord =  getArguments().getString(getString(searchword));
        int  searchType = getArguments().getInt(getString(searchtype));
        if (savedInstanceState != null)
        {
            presenter = savedInstanceState.getParcelable(getString(R.string.presenter));
            if (presenter != null){
                presenter.setViewInterface(this);
                mealsAdapter.setMeals(presenter.getResults(),getContext(),this,this);
            }else{
                presenter = new SearchResultsPresenter(Repository.getInstance(RemoteDataSourceImpl.getInstance(), LocalDataSourceImp.getInstance(getContext())),this);
                sendDataRequest(searchWord,searchType);
            }
        }else {
            presenter = new SearchResultsPresenter(Repository.getInstance(RemoteDataSourceImpl.getInstance(), LocalDataSourceImp.getInstance(getContext())),this);
            sendDataRequest(searchWord,searchType);
        }
    }


    private void sendDataRequest(String searchWord ,int searchType)
    {


        switch (searchType) {
            case ingredient: {
                presenter.filterMealsByIngredient(searchWord);
                break;
            }

            case category: {

                presenter.filterMealsByCategory(searchWord);

                break;
            }

            case country: {
                presenter.filterMealsByCountry(searchWord);
                break;
            }
        }
    }


    private void addTypeObserver()
    {
        NavBackStackEntry backStackEntry = controller.getCurrentBackStackEntry();
        backStackEntry.getSavedStateHandle().getLiveData(getString(R.string.type)).observe(getViewLifecycleOwner(),type -> {
            if (type != null) {
                switch ((Integer) type) {
                    case breakfast: {
                        addRecordToFirebaseAndRoom(getString(breakfast),presenter.getFullDetailedMeal(),(meal)-> {
                            presenter.insertMealToBreakfast(meal);
                            return null;

                        });
                        break;
                    }

                    case launch: {
                        addRecordToFirebaseAndRoom(getString(launch),presenter.getFullDetailedMeal(),(meal)-> {
                            presenter.insertMealToLaunch(meal);
                            return null;
                        });
                        break;
                    }

                    case dinner: {
                        addRecordToFirebaseAndRoom(getString(dinner),presenter.getFullDetailedMeal(),(meal)-> {
                            presenter.insertMealToDinner(meal);
                            return null;
                        });
                        break;

                    }

                    case favourites:{
                        addRecordToFirebaseAndRoom(getString(favourites),presenter.getFullDetailedMeal(),(meal)-> {

                            presenter.insertMealToFavourite(meal);
                            return null;

                        });
                        break;

                    }
                }
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
                    presenter.getFullDetailedMeal().setDay(type.toString());
                    controller.navigate(NavGraphDirections.actionToAddDialogFragment(presenter.getFullDetailedMeal()));
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
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

    }


    private void enableInteraction() {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        binding.progressBar2.setVisibility(View.INVISIBLE);
    }


    private void disableInteraction() {
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        binding.progressBar2.setVisibility(View.VISIBLE);
    }




    private void setRecyclerView()
    {
        FlexboxLayoutManager layoutManager = new CustomFlexLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);

        binding.mealsRecycler.setLayoutManager(layoutManager);
        binding.mealsRecycler.setAdapter(mealsAdapter);
        binding.mealsRecycler.setNestedScrollingEnabled(false);

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.presenter),presenter);
    }

    @Override
    public void onResultsSuccessCallback(List<FilterMeal> meals) {
        mealsAdapter.setMeals(meals,getContext(),this,this);
    }

    @Override
    public void onResultsFailureCallback(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetDetailsSuccessCallback(Meal meal,String requester) {


        enableInteraction();
        if (requester.equals(getString(add))){
            if (mAuth.getCurrentUser() !=null) {
                controller.navigate(NavGraphDirections.actionToDatePickerFragment(meal));
            }else{
                Toast.makeText(getContext(), getString(you_need_to_be_authenticated_to_do_this_action_please_login), Toast.LENGTH_SHORT).show();
            }


        }else{
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(transitionView, transitionView.getTransitionName())
                    .build();
            controller.navigate(NavGraphDirections.actionToMealDetailsFragment(transitionView.getTransitionName(),meal),extras);

        }
    }

    @Override
    public void onGetDetailsFailureCallback(String error) {

        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFilterMealAddClicked(FilterMeal meal) {
        presenter.getFullDetailsMealRequest(meal.getIdMeal(),getString(R.string.add));
        disableInteraction();

    }

    @Override
    public void onFilterMealClickListener(FilterMeal meal, ImageView transitionView) {


        this.transitionView = transitionView;
        presenter.getFullDetailsMealRequest(meal.getIdMeal(),getString(R.string.details));
    }
}