package com.example.foodplanner.meals.detailsfragment.view;

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

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.RoutinesViewHolder>  {

    private List<String> ingredients = new ArrayList<>();
    private Context context;




    @NonNull
    @Override
    public RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoutinesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinesViewHolder holder, int position) {

       String ingredientName =  ingredients.get(position);
       if (ingredientName != null){
           holder.ingredientTextView.setText(ingredientName);
           Glide.with(context)
                   .load("https://www.themealdb.com/images/ingredients/"+ingredientName+".png")
                   .override(300, 200).downsample(DownsampleStrategy.CENTER_INSIDE)
                   .into(holder.ingredientImageView);
       }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void setIngredients(List<String> ingredients, Context context)
    {

        this.context = context;
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }




    static class RoutinesViewHolder extends  RecyclerView.ViewHolder{

        TextView ingredientTextView;
        ImageView ingredientImageView;

        public RoutinesViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientImageView =  itemView.findViewById(R.id.ingredientImageView);
            ingredientTextView = itemView.findViewById(R.id.ingredientTextView);
        }
    }



}
