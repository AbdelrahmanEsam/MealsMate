package com.example.utils;

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

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.RoutinesViewHolder> {

    private List<String> cards = new ArrayList<>();
    private List<String> selectedCards = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoutinesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.routines_element,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinesViewHolder holder, int position) {

        holder.routineTextView.setText(cards.get(position));

        holder.layout.setOnClickListener(view -> {
            String clickedRoutine = cards.get(position);
            if (selectedCards.contains(clickedRoutine)){
                holder.strokeCardView.setCardBackgroundColor(context.getResources().getColor(R.color.black));
                selectedCards.remove(clickedRoutine);
            }else{
                holder.strokeCardView.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
                selectedCards.add(clickedRoutine);
            }

        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void setCards(List<String> cards , Context context)
    {
        this.context = context;
        this.cards = cards;
        notifyDataSetChanged();
    }

    public List<String> getSelectedCards()
    {

        return selectedCards;
    }

    static class RoutinesViewHolder extends  RecyclerView.ViewHolder{

        TextView routineTextView;
        CardView strokeCardView,innerCardView;
        View layout;

        public RoutinesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layout = itemView;
            routineTextView = itemView.findViewById(R.id.routineTextView);
            strokeCardView = itemView.findViewById(R.id.strokeCardView);
            innerCardView = itemView.findViewById(R.id.innerCardView);
        }
    }
}
