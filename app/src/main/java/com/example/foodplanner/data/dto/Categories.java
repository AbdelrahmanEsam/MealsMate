package com.example.foodplanner.data.dto;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Categories{

	@SerializedName("categories")
	private List<CategoriesItem> categories;

	public void setCategories(List<CategoriesItem> categories){
		this.categories = categories;
	}

	public List<CategoriesItem> getCategories(){
		return categories;
	}
}