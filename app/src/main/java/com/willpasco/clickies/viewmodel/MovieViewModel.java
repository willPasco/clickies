package com.willpasco.clickies.viewmodel;

import android.app.Application;

import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.repository.MovieRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;
    private MutableLiveData<List<Movie>> listMovieMutableLiveData;
    private LiveData<List<Movie>> listFavoriteMovieLiveData;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        listMovieMutableLiveData = new MutableLiveData<>();
        listFavoriteMovieLiveData = repository.getFavoriteListMovieLiveData();
    }

    public MutableLiveData<List<Movie>> getListMovieLiveData() {
        return listMovieMutableLiveData;
    }

    public void fetchData(String order) {
        repository.fetchData(listMovieMutableLiveData, order);
    }

    public LiveData<List<Movie>> getFavoriteListMovieLiveData() {
        listFavoriteMovieLiveData = repository.getFavoriteListMovieLiveData();
        return listFavoriteMovieLiveData;
    }
}
