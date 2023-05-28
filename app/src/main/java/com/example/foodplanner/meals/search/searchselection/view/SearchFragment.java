package com.example.foodplanner.meals.search.searchselection.view;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.data.remote.ApiProvider;
import com.example.foodplanner.data.remote.RemoteDataSourceImpl;
import com.example.foodplanner.data.dto.AllCategoriesResponse;
import com.example.foodplanner.data.dto.Area;
import com.example.foodplanner.data.dto.AreaResponse;
import com.example.foodplanner.data.dto.IngredientsResponse;
import com.example.foodplanner.data.dto.MealCategory;
import com.example.foodplanner.data.dto.MealsItem;
import com.example.foodplanner.databinding.FragmentSearchBinding;
import com.example.foodplanner.meals.detailsfragment.view.IngredientsAdapter;
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
    IngredientsAdapter ingredientsAdapter = new IngredientsAdapter();

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



    private void setUpRecyclerView(Adapter adapter, RecyclerView recyclerView)
    {
       recyclerView.setLayoutManager(setUpLayoutManager());
        recyclerView.setAdapter(adapter);
    }
}




