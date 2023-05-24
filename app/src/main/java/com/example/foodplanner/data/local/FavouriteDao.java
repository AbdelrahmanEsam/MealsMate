package com.example.foodplanner.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodplanner.data.dto.Meal;
import com.example.foodplanner.data.dto.table.Favourite;
import com.example.foodplanner.data.dto.table.Launch;

import java.util.List;


@Dao
public interface FavouriteDao {



        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Favourite meal);

        @Update
        void update(Favourite meal);

        @Delete
        void delete(Favourite meal);

        @Query("SELECT * FROM Favourite")
        LiveData<List<Favourite>> getAllMeals();

}
