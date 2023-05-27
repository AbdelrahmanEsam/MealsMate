package com.example.foodplanner.boarding.presentation.allergies.view;

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

public class AllergiesAdapter extends RecyclerView.Adapter<AllergiesAdapter.AllergiesViewHolder> {

    private List<String> allergies = new ArrayList<>();
    private List<String> selectedAllergies = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public AllergiesAdapter.AllergiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllergiesAdapter.AllergiesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_element,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllergiesViewHolder holder, int position) {
        holder.routineTextView.setText(allergies.get(position));

        holder.layout.setOnClickListener(view -> {
            String clickedRoutine = allergies.get(position);
            if (selectedAllergies.contains(clickedRoutine)){
                holder.strokeCardView.setCardBackgroundColor(context.getResources().getColor(R.color.black));
                selectedAllergies.remove(clickedRoutine);
            }else{
                holder.strokeCardView.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
                selectedAllergies.add(clickedRoutine);
            }

        });
    }



    @Override
    public int getItemCount() {
        return allergies.size();
    }

    public void setAllergies(List<String> allergies , Context context)
    {
        this.context = context;
        this.allergies = allergies;
        notifyDataSetChanged();
    }

    public List<String> getSelectedAllergies()
    {

        return selectedAllergies;
    }

    static class AllergiesViewHolder extends  RecyclerView.ViewHolder{

        TextView routineTextView;
        CardView strokeCardView,innerCardView;
        View layout;

        public AllergiesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layout = itemView;
            routineTextView = itemView.findViewById(R.id.routineTextView);
            strokeCardView = itemView.findViewById(R.id.strokeCardView);
            innerCardView = itemView.findViewById(R.id.innerCardView);
        }
    }
}
