package com.example.foodplanner.meals.mainmealsfragment.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
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

import com.example.foodplanner.MainActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.data.remote.ApiProvider;
import com.example.foodplanner.data.remote.RetrofitClient;
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

public class MealsFragment extends Fragment implements OnMealClickListener,OnMealAddClicked {


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



        binding.searchImageView.setOnClickListener(view12 -> {

            PopupMenu popupMenu = new PopupMenu(requireContext(), binding.searchImageView);


            popupMenu.getMenuInflater().inflate(R.menu.search_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId())
                {

                    case  R.id.byName:{

                        binding.searchInputLayout.getEditText().setText(R.string.type_in_your_favourite_dish);
                        break;
                    }

                    case  R.id.byCategory:{
                        binding.searchInputLayout.getEditText().setText(R.string.category_placeholder);


                        break;
                    }

                    case  R.id.byCountry:{
                        binding.searchInputLayout.getEditText().setText(R.string.nationality_placeholder);

                        break;
                    }


                    case  R.id.byIngredient:{
                        binding.searchInputLayout.getEditText().setText(R.string.ingredient_placeholder);
                        break;
                    }


                }
                return true;
            });

            popupMenu.show();
        });



        Retrofit client = RetrofitClient.getClient();

        client.create(ApiProvider.class).searchMealsByName("").enqueue(new Callback<MealsResponse>() {

            @Override
            public void onResponse(Call<MealsResponse> call, Response<MealsResponse> response) {
                MealsResponse myResponse =  response.body();
                adapter.setMeals(myResponse.getMeals(),getContext(),MealsFragment.this,MealsFragment.this);
            }

            @Override
            public void onFailure(Call<MealsResponse> call, Throwable t) {
                Log.d("responseYes","fail");
            }
        });



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
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);

        binding.mealsRecycler.setLayoutManager(layoutManager);
        binding.mealsRecycler.setAdapter(adapter);
        binding.mealsRecycler.setNestedScrollingEnabled(false);

    }

    @Override
    public void onMealClickListener(Meal meal, ImageView transitionView) {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(transitionView, transitionView.getTransitionName())
                .build();
        controller.navigate(MealsFragmentDirections.actionMealsFragmentToMealDetailsFragment(transitionView.getTransitionName(),meal),extras);
    }

    @Override
    public void onMealAddClicked(Meal meal) {
        controller.navigate(MealsFragmentDirections.actionMealsFragmentToAddDialogFragment());
    }
}