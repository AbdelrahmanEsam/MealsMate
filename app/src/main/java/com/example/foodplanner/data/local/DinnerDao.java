package com.example.foodplanner.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.table.Dinner;
import com.example.foodplanner.data.dto.table.Launch;

import java.util.List;


@Dao
public interface DinnerDao {



        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Dinner meal);

        @Update
        void update(Dinner meal);

        @Delete
        void delete(Dinner meal);

        @Query("SELECT * FROM Dinner")
        LiveData<List<Dinner>> getAllMeals();

}
