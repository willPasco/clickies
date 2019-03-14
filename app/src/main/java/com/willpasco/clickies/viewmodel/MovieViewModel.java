package com.willpasco.clickies.viewmodel;

import android.app.Application;

import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.repository.MovieRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;
    private LiveData<List<Movie>> listMovieLiveData;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        listMovieLiveData = repository.getListMovieLiveData();
    }

    public LiveData<List<Movie>> getListMovieLiveData() {
        return listMovieLiveData;
    }
}
