package com.example.foodplanner.meals.search.searchselection.presenter;

import android.annotation.SuppressLint;
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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

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








    @SuppressLint("CheckResult")
    @Override
    public void getAllCategories() {

        repository.getAllCategories().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(allCategoriesResponse -> {
            this.categories.addAll(allCategoriesResponse.getMealCategories().stream().map(MealCategory::getCategory).collect(Collectors.toList()));
            viewInterface.onGetAllCategoriesCallback(this.categories);
        },throwable -> {

            viewInterface.onGetAllCategoriesFailureCallback(throwable.getMessage());
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAllIngredients() {
          repository.getAllIngredients()
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread()).subscribe(ingredientsResponse -> {
                      this.ingredients.addAll(ingredientsResponse.getMeals().stream()
                              .map(Ingredient::getStrIngredient).collect(Collectors.toList()));
                      viewInterface.onGetAllIngredientsCallback(this.ingredients);
          },throwable -> {

                      viewInterface.onGetAllIngredientsFailureCallback(throwable.getMessage());
                  });
    }
    @SuppressLint("CheckResult")
    @Override
    public void getAllCountries() {
        repository.getAllCountries()  .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(areaResponse -> {
                    this.countries.addAll(areaResponse.getAreas().stream().map(Area::getArea).filter(s -> !s.equals("Unknown")).collect(Collectors.toList()));
                    viewInterface.onGetAllCountriesCallback(this.countries);
                },throwable -> {
                    viewInterface.onGetAllCountriesFailureCallback(throwable.getMessage());
                });
    }
}
