package com.example.foodplanner.data.dto;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MealsResponse{

	@SerializedName("meals")
	private List<Meal> meals;

	public List<Meal> getMeals(){
		return meals;
	}
}