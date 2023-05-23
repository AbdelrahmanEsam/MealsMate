package com.calendar.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.Meal;

import java.util.ArrayList;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DaysViewHolder>  {

    private List<Meal> meals = new ArrayList<>();
    private Context context;
    private OnDayClicked listener;




    @NonNull
    @Override
    public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DaysViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DaysViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void setDays(List<Meal> meals, Context context,OnDayClicked listener)
    {
        this.listener = listener;
        this.context = context;
        this.meals = meals;
        notifyDataSetChanged();
    }




    static class DaysViewHolder extends  RecyclerView.ViewHolder{


        TextView dayTextView,numberTextView;


        public DaysViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);



        }
    }



}
