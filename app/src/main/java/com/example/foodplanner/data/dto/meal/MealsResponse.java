package com.example.foodplanner.data.dto.meal;

import java.util.List;

import com.example.foodplanner.data.dto.Meal;
import com.google.gson.annotations.SerializedName;

public class MealsResponse{

	@SerializedName("meals")
	private List<Meal> meals;

	public List<Meal> getMeals(){
		return meals;
	}
}