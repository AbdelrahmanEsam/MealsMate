package com.example.foodplanner.meals.search.searchselection.presenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.foodplanner.data.dto.area.Area;
import com.example.foodplanner.data.dto.category.MealCategory;
import com.example.foodplanner.data.dto.ingredients.Ingredient;
import com.example.foodplanner.data.repository.RepositoryInterface;
import com.example.foodplanner.meals.search.searchselection.view.SearchFragmentViewInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchSelectionPresenter implements SearchSelectionPresenterInterface, Parcelable {

    private  RepositoryInterface repository = null ;
    private  SearchFragmentViewInterface viewInterface = null;

    private List<String> categories = new ArrayList<>();

    private List<String> ingredients = new ArrayList<>();

    private List<String> countries = new ArrayList<>();

    public SearchSelectionPresenter(RepositoryInterface repository, SearchFragmentViewInterface viewInterface)
    {
        this.repository = repository;
        this.viewInterface = viewInterface;
    }

    protected SearchSelectionPresenter(Parcel in) {
        categories = in.createStringArrayList();
        ingredients = in.createStringArrayList();
        countries = in.createStringArrayList();
    }

    public static final Creator<SearchSelectionPresenter> CREATOR = new Creator<SearchSelectionPresenter>() {
        @Override
        public SearchSelectionPresenter createFromParcel(Parcel in) {
            return new SearchSelectionPresenter(in);
        }

        @Override
        public SearchSelectionPresenter[] newArray(int size) {
            return new SearchSelectionPresenter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(categories);
        dest.writeStringList(ingredients);
        dest.writeStringList(countries);
    }


    public List<String> getCategories() {
        return categories;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getCountries() {
        return countries;
    }









    @Override
    public void getAllCategories() {

        repository.getAllCategories(this);
    }

    @Override
    public void getAllIngredients() {
          repository.getAllIngredients(this);
    }

    @Override
    public void getAllCountries() {
        repository.getAllCountries(this);
    }

    @Override
    public void onGetAllCategoriesSuccessCallback(List<MealCategory> categories) {

        this.categories.addAll(categories.stream().map(MealCategory::getCategory).collect(Collectors.toList()));
        viewInterface.onGetAllCategoriesCallback(this.categories);
    }

    @Override
    public void onGetAllIngredientsSuccessCallback(List<Ingredient> ingredients) {

        this.ingredients.addAll(ingredients.stream().map(Ingredient::getStrIngredient).collect(Collectors.toList()));
        viewInterface.onGetAllIngredientsCallback(this.ingredients);
    }

    @Override
    public void onGetAllCountriesSuccessCallback(List<Area> countries) {

        this.countries.addAll(countries.stream().map(Area::getArea).filter(s -> !s.equals("Unknown")).collect(Collectors.toList()));
        viewInterface.onGetAllCountriesCallback(this.countries);

    }

    @Override
    public void onGetAllCategoriesFailureCallback(String error) {

        viewInterface.onGetAllCategoriesFailureCallback(error);
    }

    @Override
    public void onGetAllIngredientsFailureCallback(String error) {

        viewInterface.onGetAllIngredientsFailureCallback(error);
    }

    @Override
    public void onGetAllCountriesFailureCallback(String error) {

        viewInterface.onGetAllCountriesFailureCallback(error);
    }
}
