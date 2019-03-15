package com.willpasco.clickies.viewmodel;

import android.app.Application;

import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.model.Review;
import com.willpasco.clickies.model.Trailer;
import com.willpasco.clickies.repository.MovieRepository;
import com.willpasco.clickies.service.DataWrapper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;
    private MutableLiveData<DataWrapper<List<Movie>>> listMovieMutableLiveData;
    private MutableLiveData<DataWrapper<List<Trailer>>> listTrailerMutableLiveData;
    private MutableLiveData<DataWrapper<List<Review>>> listReviewMutableLiveData;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        listMovieMutableLiveData = new MutableLiveData<>();
        listTrailerMutableLiveData = new MutableLiveData<>();
        listReviewMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapper<List<Movie>>> getListMovieLiveData() {
        return listMovieMutableLiveData;
    }

    public void fetchData(String order) {
        repository.fetchData(listMovieMutableLiveData, order);
    }

    public LiveData<List<Movie>> getFavoriteListMovieLiveData() {
        return repository.getFavoriteListMovieLiveData();
    }

    public MutableLiveData<DataWrapper<List<Trailer>>> getListTrailerMutableLiveData() {
        return listTrailerMutableLiveData;
    }

    public boolean isFavoriteMovie(int id) {
        return repository.isFavoriteMovie(id);
    }

    public void setMovieFavorite(Movie model) {
        repository.insertMovie(model);
    }

    public void setMovieUnFavorite(Movie model) {
        repository.deleteMovie(model);
    }

    public void loadTrailers(int id) {
        repository.loadTrailer(listTrailerMutableLiveData, id);
    }

    public MutableLiveData<DataWrapper<List<Review>>> getListReviewMutableLiveData() {
        return listReviewMutableLiveData;
    }

    public void loadReviews(int id) {
        repository.loadReviews(listReviewMutableLiveData, id);
    }
}
