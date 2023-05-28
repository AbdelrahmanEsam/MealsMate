package com.example.foodplanner.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodplanner.data.dto.table.Breakfast;

import java.util.List;


@Dao
public interface BreakfastDao {



        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Breakfast meal);

        @Update
        void update(Breakfast meal);

        @Delete
        void delete(Breakfast meal);

        @Query("SELECT * FROM Breakfast")
        LiveData<List<Breakfast>> getAllMeals();

}
