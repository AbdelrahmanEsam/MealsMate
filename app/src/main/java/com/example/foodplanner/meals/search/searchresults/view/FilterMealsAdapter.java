package com.example.foodplanner.meals.search.searchresults.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.search.FilterMeal;
import com.example.foodplanner.meals.mainmealsfragment.view.OnMealAddClickedListener;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class FilterMealsAdapter extends RecyclerView.Adapter<FilterMealsAdapter.MealsViewHolder>  {

    private List<FilterMeal> meals = new ArrayList<>();
    private Context context;
    private OnFilterMealClickListener itemListener;

    private OnFilterMealAddClickedListener addListener;




    @NonNull
    @Override
    public MealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MealsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MealsViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        holder.itemView.setAnimation(animation);

        FilterMeal meal = meals.get(position);
        holder.mealNameTextView.setText(meal.getStrMeal());
        holder.mealImage.setTransitionName("mealImage"+position);
        Glide.with(context)
                .load(meal.getStrMealThumb())
                .override(300, 200).downsample(DownsampleStrategy.CENTER_INSIDE)
                .into(holder.mealImage);

        holder.layout.setOnClickListener(view -> {
            itemListener.onFilterMealClickListener(meal,holder.mealImage);
        });


        holder.addImageView.setOnClickListener(view -> {
            addListener.onFilterMealAddClicked(meal);
        });

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void setMeals(List<FilterMeal> meals, Context context, OnFilterMealClickListener itemListener, OnFilterMealAddClickedListener addListener)
    {
        this.addListener = addListener;
        this.itemListener = itemListener;
        this.context = context;
        this.meals = meals;
        notifyDataSetChanged();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MealsViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.itemView.clearAnimation();
    }

    static class MealsViewHolder extends  RecyclerView.ViewHolder{

        TextView mealNameTextView,ingredientTextView;
        ImageView mealImage,addImageView;
        View layout;
        MaterialCardView cardView;

        public MealsViewHolder(@NonNull View itemView) {
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
