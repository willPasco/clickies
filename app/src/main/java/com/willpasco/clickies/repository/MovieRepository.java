package com.willpasco.clickies.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.willpasco.clickies.HomeActivity;
import com.willpasco.clickies.dao.ClickiesRoomDatabase;
import com.willpasco.clickies.dao.MovieDao;
import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.model.MovieJsonResponse;
import com.willpasco.clickies.model.Trailer;
import com.willpasco.clickies.model.TrailerJsonResponse;
import com.willpasco.clickies.service.RetrofitService;
import com.willpasco.clickies.service.ServiceGenerator;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.willpasco.clickies.service.ServiceGenerator.API_KEY;

public class MovieRepository {

    private MovieDao dao;

    public MovieRepository(Application application) {
        ClickiesRoomDatabase db = ClickiesRoomDatabase.getDatabase(application);
        this.dao = db.movieDao();
    }


    public LiveData<List<Movie>> getFavoriteListMovieLiveData() {
        return dao.getFavoriteMovies();
    }

    public void fetchData(final MutableLiveData<List<Movie>> listMovieMutableLiveData, String order) {

            RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

            Call<MovieJsonResponse> call = service.getMovies(order, API_KEY);

            call.enqueue(new Callback<MovieJsonResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieJsonResponse> call, @NonNull Response<MovieJsonResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            listMovieMutableLiveData.setValue( response.body().getResults());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieJsonResponse> call, @NonNull Throwable t) {
                }
            });
    }

    public boolean isFavoriteMovie(int id) {
        Movie movie = dao.getFavoriteMovie(id);
        return movie != null;
    }

    public void insertMovie(Movie model) {
        model.setFavorite(true);
        new InsertAsyncTask().execute(model);
    }

    public void deleteMovie(Movie model) {
        new DeleteAsyncTaks().execute(model);
    }

    public void loadTrailer(final MutableLiveData<List<Trailer>> listTrailerMutableLiveData, int id) {
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<TrailerJsonResponse> call = service.getTrailer(id, API_KEY);

        call.enqueue(new Callback<TrailerJsonResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrailerJsonResponse> call, @NonNull Response<TrailerJsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTrailerMutableLiveData.setValue( response.body().getResults());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailerJsonResponse> call, @NonNull Throwable t) {
            }
        });

    }

    private class DeleteAsyncTaks  extends AsyncTask<Movie, Void, Void>{
        @Override
        protected Void doInBackground(Movie... movies) {
            dao.delete(movies[0]);
            return null;
        }
    }

    private class InsertAsyncTask  extends AsyncTask<Movie, Void, Void>{
        @Override
        protected Void doInBackground(Movie... movies) {
            dao.insert(movies[0]);
            return null;
        }
    }
}
