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

    @Query("SELECT * FROM movie_table ORDER BY popularity DESC")
    List<Movie> getMoviesByPopular();

    @Query("SELECT * FROM movie_table ORDER BY vote_average DESC")
    List<Movie> getMoviesByVote();

}
