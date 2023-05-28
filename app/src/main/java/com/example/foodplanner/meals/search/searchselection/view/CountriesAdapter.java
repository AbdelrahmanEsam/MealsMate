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

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>  {

    private List<String> countries = new ArrayList<>();
    private Context context;

    private OnCountryClickedListener listener;




    @NonNull
    @Override
    public CountriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CountriesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesViewHolder holder, int position) {

       String categoryName =  countries.get(position);
       if (categoryName != null){
           holder.cardTextView.setText(categoryName);
           Glide.with(context)
                   .load(context.getResources().getStringArray(R.array.flag_urls)[position])
                   .downsample(DownsampleStrategy.FIT_CENTER)
                   .override(300, 200).downsample(DownsampleStrategy.CENTER_INSIDE)
                   .into(holder.cardImageView);

           holder.layout.setOnClickListener(view -> {

               listener.onCountryClicked(categoryName);
           });
       }
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public void setData(List<String> ingredients, Context context,OnCountryClickedListener listener)
    {

        this.listener = listener;
        this.context = context;
        this.countries = ingredients;
        notifyDataSetChanged();
    }




    static class CountriesViewHolder extends  RecyclerView.ViewHolder{

        TextView cardTextView;
        ImageView cardImageView;
        CardView layout;
        public CountriesViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.strokeCardView);
            cardImageView =  itemView.findViewById(R.id.ingredientImageView);
            cardTextView = itemView.findViewById(R.id.ingredientTextView);
        }
    }



}
