package com.example.foodplanner.schedule.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.Day;

import java.util.ArrayList;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DaysViewHolder>  {

    private List<Day> days = new ArrayList<>();
    private Context context;
    private OnDayListener listener;
    private static  int selectedDay = 0 ;





    @NonNull
    @Override
    public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DaysViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DaysViewHolder holder, int position) {


        holder.bind(days.get(position),listener);

    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public void setDays(List<Day> days, Context context, OnDayListener listener)
    {
        Log.d("day",days.size()+"");
        this.listener = listener;
        this.context = context;
        this.days = days;
        notifyDataSetChanged();
    }




    static class DaysViewHolder extends  RecyclerView.ViewHolder {


        TextView dayTextView, numberTextView;

        CardView dayCard;

        public DaysViewHolder(@NonNull View itemView) {
            super(itemView);
            dayCard = itemView.findViewById(R.id.dayCard);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);


        }


        public void bind(Day day, OnDayListener listener) {

            dayTextView.setText(day.getDayName());
            numberTextView.setText(day.getDayNumber());


            if (selectedDay != getAdapterPosition()) {
                initState();
            } else {
                selectedSate();
            }


            dayCard.setOnClickListener(view -> {
                selectedDay = getAdapterPosition();
                listener.onDayClicked(day, selectedDay);

            });
        }


        private void initState() {
            dayCard.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.gray));
            dayTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.gray20));
            numberTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
        }


        private void selectedSate() {
            dayCard.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.orange));
            dayTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
            numberTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
        }
    }
}
