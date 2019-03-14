package com.willpasco.clickies.dao;

import com.willpasco.clickies.model.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie... movies);

    @Query("SELECT * FROM movie_table WHERE favorite == 1")
    LiveData<List<Movie>> getFavoriteMovies();

    @Query("SELECT * FROM movie_table WHERE id == :id")
    Movie getFavoriteMovie(int id);

    @Delete
    void delete(Movie movie);
}
