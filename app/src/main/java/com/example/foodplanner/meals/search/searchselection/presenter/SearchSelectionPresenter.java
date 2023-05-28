package com.example.foodplanner.meals.search.searchselection.presenter;

import com.example.foodplanner.data.dto.Area;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.MealCategory;
import com.example.foodplanner.data.repository.RepositoryInterface;
import com.example.foodplanner.meals.search.searchselection.view.SearchFragmentViewInterface;

import java.util.ArrayList;
import java.util.List;

public class SearchSelectionPresenter implements  SearchSelectionPresenterInterface{

    private final RepositoryInterface repository;
    private final SearchFragmentViewInterface viewInterface;

    private List<MealCategory> categories = new ArrayList<>();

    private List<Meal> ingredients = new ArrayList<>();

    private List<Area> countries = new ArrayList<>();


    public SearchSelectionPresenter(RepositoryInterface repository, SearchFragmentViewInterface viewInterface)
    {
        this.repository = repository;
        this.viewInterface = viewInterface;
    }

    @Override
    public void getAllCategories() {
        repository.getAllCategories();
    }

    @Override
    public void getAllIngredients() {
          repository.getAllIngredients();
    }

    @Override
    public void getAllCountries() {
        repository.getAllCountries();
    }

    @Override
    public void onGetAllCategoriesCallback(List<MealCategory> categories) {

    }

    @Override
    public void onGetAllIngredientsCallback(List<Meal> ingredients) {

    }

    @Override
    public void onGetAllCountriesCallback(List<Area> countries) {

    }
}
