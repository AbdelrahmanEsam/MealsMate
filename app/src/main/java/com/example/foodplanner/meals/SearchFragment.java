package com.example.foodplanner.meals;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.data.remote.ApiProvider;
import com.example.foodplanner.data.remote.RetrofitClient;
import com.example.foodplanner.data.dto.AllCategoriesResponse;
import com.example.foodplanner.data.dto.Area;
import com.example.foodplanner.data.dto.AreaResponse;
import com.example.foodplanner.data.dto.IngredientsResponse;
import com.example.foodplanner.data.dto.MealCategory;
import com.example.foodplanner.data.dto.MealsItem;
import com.example.foodplanner.databinding.FragmentSearchBinding;
import com.example.utils.CardsAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private NavController controller;
    CardsAdapter countriesAdapter = new CardsAdapter();
    CardsAdapter categoriesAdapter = new CardsAdapter();
    CardsAdapter ingredientsAdapter = new CardsAdapter();

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


        Retrofit client = RetrofitClient.getClient();
        client.create(ApiProvider.class).getAllCategories().enqueue(new Callback<AllCategoriesResponse>() {
            @Override
            public void onResponse(Call<AllCategoriesResponse> call, Response<AllCategoriesResponse> response) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    categoriesAdapter.setCards(response.body().getMealCategories().stream().map(MealCategory::getCategory).collect(Collectors.toList()), getContext());
                }
            }

            @Override
            public void onFailure(Call<AllCategoriesResponse> call, Throwable t) {

            }
        });


        client.create(ApiProvider.class).getAllCountries().enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    countriesAdapter.setCards(response.body().getAreas().stream().map(Area::getArea).collect(Collectors.toList()), getContext());
                }
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {

            }
        });


        client.create(ApiProvider.class).getAllCategories().enqueue(new Callback<AllCategoriesResponse>() {
            @Override
            public void onResponse(Call<AllCategoriesResponse> call, Response<AllCategoriesResponse> response) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    categoriesAdapter.setCards(response.body().getMealCategories().stream().map(MealCategory::getCategory).collect(Collectors.toList()), getContext());
                }
            }

            @Override
            public void onFailure(Call<AllCategoriesResponse> call, Throwable t) {

            }
        });


        client.create(ApiProvider.class).getAllIngredients().enqueue(new Callback<IngredientsResponse>() {
            @Override
            public void onResponse(Call<IngredientsResponse> call, Response<IngredientsResponse> response) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ingredientsAdapter.setCards(response.body().getMeals().stream().map(MealsItem::getStrIngredient).collect(Collectors.toList()), getContext());
                }
            }

            @Override
            public void onFailure(Call<IngredientsResponse> call, Throwable t) {

            }
        });




        setUpRecyclerView(countriesAdapter,binding.countriesRecyclerView);
        setUpRecyclerView(categoriesAdapter,binding.categoriesRecyclerView);
        setUpRecyclerView(ingredientsAdapter,binding.ingredientsRecyclerView);

    }


    private FlexboxLayoutManager setUpLayoutManager()
    {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        return layoutManager;
    }



    private void setUpRecyclerView(CardsAdapter adapter, RecyclerView recyclerView)
    {
       recyclerView.setLayoutManager(setUpLayoutManager());
        recyclerView.setAdapter(adapter);
    }
}




