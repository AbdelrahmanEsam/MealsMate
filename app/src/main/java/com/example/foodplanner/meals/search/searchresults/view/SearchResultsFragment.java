package com.example.foodplanner.meals.search.searchresults.view;

import static com.example.foodplanner.R.string.*;
import static com.example.foodplanner.R.string.country;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;


public class SearchResultsFragment extends Fragment
        implements  SearchResultsViewInterface,OnFilterMealClickListener,OnFilterMealAddClickedListener {


    private SearchResultsPresenter presenter ;

    private FragmentSearchResultsBinding binding;

    private final FilterMealsAdapter mealsAdapter = new FilterMealsAdapter();

    private View transitionView;


    private NavController controller;

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
        controller = Navigation.findNavController(view);

       String searchWord =  getArguments().getString(getString(searchword));
       int  searchType = getArguments().getInt(getString(searchtype));


        if (savedInstanceState != null)
        {
            presenter = savedInstanceState.getParcelable(getString(R.string.presenter));
            mealsAdapter.setMeals(presenter.getResults(),getContext(),this,this);
        }else {
            presenter = new SearchResultsPresenter(Repository.getInstance(RemoteDataSourceImpl.getInstance(), LocalDataSourceImp.getInstance(getContext())),this);
            sendDataRequest(searchWord,searchType);
        }

        setRecyclerView();
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
    public void onGetDetailsSuccessCallback(Meal meal) {

        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(transitionView, transitionView.getTransitionName())
                .build();
        controller.navigate(NavGraphDirections.actionToMealDetailsFragment(transitionView.getTransitionName(),meal),extras);
    }

    @Override
    public void onGetDetailsFailureCallback(String error) {

        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFilterMealAddClicked(FilterMeal meal) {

    }

    @Override
    public void onFilterMealClickListener(FilterMeal meal, ImageView transitionView) {


        this.transitionView = transitionView;
        presenter.getFullDetailsMealRequest(meal.getIdMeal());
    }
}