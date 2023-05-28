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

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>  {

    private List<String> categories = new ArrayList<>();
    private Context context;

    private OnCategoryClickedListener listener;




    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

       String categoryName =  categories.get(position);
       if (categoryName != null){
           holder.cardTextView.setText(categoryName);
           Glide.with(context)
                   .load("https://www.themealdb.com//images//category//"+categoryName+".png")
                   .downsample(DownsampleStrategy.FIT_CENTER)
                   .override(300, 200).downsample(DownsampleStrategy.CENTER_INSIDE)
                   .into(holder.cardImageView);


           holder.layout.setOnClickListener(view -> {
               listener.onCategoryClicked(categoryName);
           });

       }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setData(List<String> ingredients, Context context,OnCategoryClickedListener listener)
    {
        this.listener = listener;
        this.context = context;
        this.categories = ingredients;
        notifyDataSetChanged();
    }




    static class CategoryViewHolder extends  RecyclerView.ViewHolder{

        TextView cardTextView;
        ImageView cardImageView;

        CardView layout;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.strokeCardView);
            cardImageView =  itemView.findViewById(R.id.ingredientImageView);
            cardTextView = itemView.findViewById(R.id.ingredientTextView);
        }
    }



}
