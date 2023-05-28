package com.example.foodplanner.meals.search.searchselection.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.example.foodplanner.R;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>  {

    private List<String> ingredients = new ArrayList<>();
    private Context context;

    private OnIngredientClickedListener listener;




    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {

       String ingredientName =  ingredients.get(position);
       if (ingredientName != null){
           holder.ingredientTextView.setText(ingredientName);
           Glide.with(context)
                   .load("https://www.themealdb.com/images/ingredients/"+ingredientName+".png")
                   .override(300, 200).downsample(DownsampleStrategy.CENTER_INSIDE)
                   .into(holder.ingredientImageView);

           holder.layout.setOnClickListener(view -> {

              listener.onIngredientClicked(ingredientName);
           });
       }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void setData(List<String> ingredients, Context context,OnIngredientClickedListener listener)
    {

        this.listener = listener;
        this.context = context;
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }




    static class IngredientsViewHolder extends  RecyclerView.ViewHolder{

        TextView ingredientTextView;
        ImageView ingredientImageView;

        CardView layout;
        public IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.strokeCardView);
            ingredientImageView =  itemView.findViewById(R.id.ingredientImageView);
            ingredientTextView = itemView.findViewById(R.id.ingredientTextView);
        }
    }



}
