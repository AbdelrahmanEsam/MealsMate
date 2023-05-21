package com.example.foodplanner.meals;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodplanner.MainActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.data.ApiProvider;
import com.example.foodplanner.data.RetrofitClient;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.MealsResponse;
import com.example.foodplanner.databinding.FragmentMealsBinding;
import com.example.utils.CustomFlexLayoutManager;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MealsFragment extends Fragment {


    private FragmentMealsBinding binding;
    private NavController controller;
    private List<Meal> meals = new ArrayList<>();
    private MealsAdapter adapter  = new MealsAdapter();

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
        controller = Navigation.findNavController(view);

        binding.mealOfTheDayBarrier.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "yes", Toast.LENGTH_SHORT).show();
        });

        Retrofit client = RetrofitClient.getClient();

        client.create(ApiProvider.class).searchMealsByName("").enqueue(new Callback<MealsResponse>() {

            @Override
            public void onResponse(Call<MealsResponse> call, Response<MealsResponse> response) {
               MealsResponse myResponse =  response.body();
                adapter.setMeals(myResponse.getMeals(),getContext());
            }

            @Override
            public void onFailure(Call<MealsResponse> call, Throwable t) {

            }
        });



        adapter.setMeals(meals,getContext());
        setRecyclerView();

    }



    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }


    private void setRecyclerView()
    {
        FlexboxLayoutManager layoutManager = new CustomFlexLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        binding.mealsRecycler.setLayoutManager(layoutManager);
        binding.mealsRecycler.setAdapter(adapter);
        binding.mealsRecycler.setNestedScrollingEnabled(false);

    }
}