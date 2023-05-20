package com.example.foodplanner.boarding.presentation.favourites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;

import java.util.ArrayList;
import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.RoutinesViewHolder> {

    private List<String> favourites = new ArrayList<>();
    private List<String> selectedFavourites = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoutinesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinesViewHolder holder, int position) {

        holder.favouriteTextView.setText(favourites.get(position));

        holder.layout.setOnClickListener(view -> {
            String clickedFavourite = favourites.get(position);
            if (selectedFavourites.contains(clickedFavourite)){
                holder.strokeCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                selectedFavourites.remove(clickedFavourite);
            }else{
                holder.strokeCardView.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
                selectedFavourites.add(clickedFavourite);
            }

        });
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    public void setFavourites(List<String> favourites , Context context)
    {
        this.context = context;
        this.favourites = favourites;
        notifyDataSetChanged();
    }

    public List<String> getSelectedFavourites()
    {

        return selectedFavourites;
    }

    static class RoutinesViewHolder extends  RecyclerView.ViewHolder{

        TextView favouriteTextView;
        CardView strokeCardView,innerCardView;
        View layout;

        public RoutinesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layout = itemView;
            favouriteTextView = itemView.findViewById(R.id.favouriteTextView);
            strokeCardView = itemView.findViewById(R.id.strokeCardView);
            innerCardView = itemView.findViewById(R.id.innerCardView);
        }
    }
}
