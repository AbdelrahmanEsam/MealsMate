package com.example.foodplanner.data.dto.ingredients;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class IngredientsResponse{

	@SerializedName("meals")
	private List<Ingredient> meals;

	public List<Ingredient> getMeals(){
		return meals;
	}
}