package com.willpasco.clickies.dao;

import com.willpasco.clickies.model.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie... movies);

    @Query("SELECT * from movie_table")
    LiveData<List<Movie>> getAllMovies();

}
