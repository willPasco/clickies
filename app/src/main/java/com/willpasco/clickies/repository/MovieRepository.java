package com.willpasco.clickies.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.willpasco.clickies.dao.ClickiesRoomDatabase;
import com.willpasco.clickies.dao.MovieDao;
import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.model.MovieJsonResponse;
import com.willpasco.clickies.service.RetrofitService;
import com.willpasco.clickies.service.ServiceGenerator;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.willpasco.clickies.service.ServiceGenerator.API_KEY;

public class MovieRepository {
    private MovieDao dao;
    private LiveData<List<Movie>> listMovieLiveData;

    public MovieRepository(Application application) {
        ClickiesRoomDatabase db = ClickiesRoomDatabase.getDatabase(application);
        this.dao = db.movieDao();
        listMovieLiveData = dao.getAllMovies();
    }

    public LiveData<List<Movie>> getListMovieLiveData() {
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<MovieJsonResponse> call = service.getMovies("popular", API_KEY);

        call.enqueue(new Callback<MovieJsonResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieJsonResponse> call, @NonNull Response<MovieJsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Movie> moviesList = response.body().getResults();
                        Movie[] moviesArray = new Movie[moviesList.size()];
                        new InsertAsyncTask(dao).execute(moviesList.toArray(moviesArray));
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieJsonResponse> call, @NonNull Throwable t) {
            }
        });
        return listMovieLiveData;
    }

    private static class InsertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDao asyncTaskDao;

        public InsertAsyncTask(MovieDao asyncTaskDao) {
            this.asyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            asyncTaskDao.insert(movies);
            return null;
        }
    }

}
