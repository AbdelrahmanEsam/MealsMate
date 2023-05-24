package com.example.foodplanner.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodplanner.data.dto.table.Breakfast;
import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.table.Launch;

import java.util.List;


@Dao
public interface LaunchDao {



        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Launch meal);

        @Update
        void update(Launch meal);

        @Delete
        void delete(Launch meal);

        @Query("SELECT * FROM Launch")
        LiveData<List<Launch>> getAllMeals();

}
