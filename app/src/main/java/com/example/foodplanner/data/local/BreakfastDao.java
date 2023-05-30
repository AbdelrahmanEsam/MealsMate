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

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;


@Dao
public interface BreakfastDao {



        @Insert(onConflict = OnConflictStrategy.REPLACE)
        Completable  insert(Breakfast meal);

        @Update
        Completable  update(Breakfast meal);

        @Delete
        Completable delete(Breakfast meal);

        @Query("SELECT * FROM Breakfast")
        Observable<List<Breakfast>> getAllMeals();

}
