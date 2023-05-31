package com.example.foodplanner.meals.mainmealsfragment.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.foodplanner.NavGraphDirections;
import com.example.foodplanner.R;
import com.example.foodplanner.data.dto.Meal;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {



    private NavController controller;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        controller = NavHostFragment.findNavController(this);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.DatePickerTheme, this, year, month, day);
    dialog.getDatePicker().setMinDate(calendar.getTime().getTime());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
    dialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
        return dialog;
    }



    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d("dateListener","onDateSet");
        controller.getPreviousBackStackEntry().getSavedStateHandle().set(getString(R.string.date),day);
    }
}