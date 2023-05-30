package com.example.foodplanner.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodplanner.data.dto.table.Favourite;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;


@Dao
public interface FavouriteDao {



        @Insert(onConflict = OnConflictStrategy.REPLACE)
        Completable  insert(Favourite meal);

        @Update
        Completable update(Favourite meal);

        @Delete
        Completable  delete(Favourite meal);

        @Query("SELECT * FROM Favourite")
        Observable<List<Favourite>> getAllMeals();

}
