package com.example.foodplanner.meals.search.searchselection.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.data.local.LocalDataSourceImp;
import com.example.foodplanner.data.remote.RemoteDataSourceImpl;
import com.example.foodplanner.data.repository.Repository;
import com.example.foodplanner.databinding.FragmentSearchBinding;
import com.example.foodplanner.meals.search.searchselection.presenter.SearchSelectionPresenter;

import java.util.List;


public class SearchFragment extends Fragment implements  SearchFragmentViewInterface,OnCategoryClickedListener,OnCountryClickedListener,OnIngredientClickedListener {

    private FragmentSearchBinding binding;
    private NavController controller;
    CountriesAdapter countriesAdapter = new CountriesAdapter();
    CategoriesAdapter categoriesAdapter = new CategoriesAdapter();
    IngredientsAdapter ingredientsAdapter = new IngredientsAdapter();

    private SearchSelectionPresenter presenter ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        if (savedInstanceState != null)
        {
            presenter = savedInstanceState.getParcelable(getString(R.string.presenter));
            if (presenter != null){
                ingredientsAdapter.setData(presenter.getIngredients(),getContext(),this);
                categoriesAdapter.setData(presenter.getCategories(),getContext(),this);
                countriesAdapter.setData(presenter.getCountries(),getContext(),this);
            }  else{
                initPresenterAndSendRequests();
            }
        }else {


            initPresenterAndSendRequests();

        }


        setUpRecyclerView(countriesAdapter,binding.countriesRecyclerView);
        setUpRecyclerView(categoriesAdapter,binding.categoriesRecyclerView);
        setUpRecyclerView(ingredientsAdapter,binding.ingredientsRecyclerView);
    }

    private void initPresenterAndSendRequests()
    {
        presenter = new SearchSelectionPresenter(
                Repository.getInstance(RemoteDataSourceImpl.getInstance(), LocalDataSourceImp.getInstance(getContext())),this);
        presenter.getAllCategories();
        presenter.getAllCountries();
        presenter.getAllIngredients();
    }




    private LinearLayoutManager setUpLayoutManager()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        return layoutManager;
    }



    private void setUpRecyclerView(Adapter adapter, RecyclerView recyclerView)
    {
       recyclerView.setLayoutManager(setUpLayoutManager());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.presenter),presenter);

    }

    @Override
    public void onGetAllCategoriesCallback(List<String> categories) {
        categoriesAdapter.setData(categories,getContext(),this);
    }

    @Override
    public void onGetAllIngredientsCallback(List<String> ingredients) {
        ingredientsAdapter.setData(ingredients, getContext(),this);
    }

    @Override
    public void onGetAllCountriesCallback(List<String> countries) {

        countriesAdapter.setData(countries, getContext(),this);
    }

    @Override
    public void onGetAllCategoriesFailureCallback(String error) {

    }

    @Override
    public void onGetAllIngredientsFailureCallback(String error) {

    }

    @Override
    public void onGetAllCountriesFailureCallback(String error) {

    }

    @Override
    public void onCategoryClicked(String category) {

        controller.navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment(category,R.string.category));
    }

    @Override
    public void onCountryClicked(String country) {

        controller.navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment(country,R.string.country));

    }

    @Override
    public void onIngredientClicked(String ingredient) {
        controller.navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment(ingredient,R.string.ingredient));

    }
}




