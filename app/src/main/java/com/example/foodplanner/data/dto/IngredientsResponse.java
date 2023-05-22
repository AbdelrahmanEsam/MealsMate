package com.example.foodplanner.data.dto;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class IngredientsResponse{

	@SerializedName("meals")
	private List<MealsItem> meals;

	public List<MealsItem> getMeals(){
		return meals;
	}
}