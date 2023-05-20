package com.example.foodplanner.boarding.presentation.categories;

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

public class RoutinesAdapter extends RecyclerView.Adapter<RoutinesAdapter.RoutinesViewHolder> {

    private List<String> routines = new ArrayList<>();
    private List<String> selectedRoutines = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoutinesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.routines_element,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinesViewHolder holder, int position) {

        holder.routineTextView.setText(routines.get(position));

        holder.layout.setOnClickListener(view -> {
            String clickedRoutine = routines.get(position);
            if (selectedRoutines.contains(clickedRoutine)){
                holder.strokeCardView.setCardBackgroundColor(context.getResources().getColor(R.color.black));
                selectedRoutines.remove(clickedRoutine);
            }else{
                holder.strokeCardView.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
                selectedRoutines.add(clickedRoutine);
            }

        });
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    public void setRoutines(List<String> routines , Context context)
    {
        this.context = context;
        this.routines = routines;
        notifyDataSetChanged();
    }

    public List<String> getSelectedRoutines()
    {

        return selectedRoutines;
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
