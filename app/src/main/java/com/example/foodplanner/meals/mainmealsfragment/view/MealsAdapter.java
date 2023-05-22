package com.example.foodplanner.meals.mainmealsfragment.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.Meal;
import com.example.utils.OnMealClickListener;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.RoutinesViewHolder> implements OnMealClickListener {

    private List<Meal> meals = new ArrayList<>();
    private Context context;
    private  OnMealClickListener listener;



    @NonNull
    @Override
    public RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoutinesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinesViewHolder holder, int position) {

        holder.itemView.post(() -> {
         if(holder.itemView.getMeasuredWidth() < holder.itemView.getWidth() ){
           holder.layout.setMinimumWidth(holder.itemView.getWidth());
         }
        });


        Meal meal = meals.get(position);
        holder.mealNameTextView.setText(meal.getStrMeal());
        holder.ingredientTextView.setText(String.format("%s, %s"
                , meal.getStrIngredient1(), meal.getStrIngredient2()
                ,meal.getStrIngredient3(),meal.getStrIngredient4()
        ));

        holder.mealImage.setTransitionName("mealImage"+position);
        Glide.with(context)
                .load(meal.getStrMealThumb())
                .override(300, 200).downsample(DownsampleStrategy.CENTER_INSIDE)
                .into(holder.mealImage);

        holder.layout.setOnClickListener(view -> {

            listener.onMealClickListener(meal,"mealImage"+position);
        });

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void setMeals(List<Meal> meals, Context context, OnMealClickListener listener)
    {
        this.listener = listener;
        this.context = context;
        this.meals = meals;
        notifyDataSetChanged();
    }

    @Override
    public void onMealClickListener(Meal meal, String transitionName) {


    }


    static class RoutinesViewHolder extends  RecyclerView.ViewHolder{

        TextView mealNameTextView,ingredientTextView;
        ImageView mealImage,addImageView;
        View layout;
        MaterialCardView cardView;

        public RoutinesViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;

            cardView = itemView.findViewById(R.id.itemCardView);
            mealNameTextView = itemView.findViewById(R.id.mealNameTextView);
            ingredientTextView = itemView.findViewById(R.id.ingredientsTextView);
            mealImage = itemView.findViewById(R.id.mealImage);
            addImageView = itemView.findViewById(R.id.plusMealImage);
        }
    }



}