package com.willpasco.clickies.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.willpasco.clickies.dao.ClickiesRoomDatabase;
import com.willpasco.clickies.dao.MovieDao;
import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.model.MovieJsonResponse;
import com.willpasco.clickies.model.Review;
import com.willpasco.clickies.model.ReviewJsonResponse;
import com.willpasco.clickies.model.Trailer;
import com.willpasco.clickies.model.TrailerJsonResponse;
import com.willpasco.clickies.service.DataWrapper;
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

    public void fetchData(final MutableLiveData<DataWrapper<List<Movie>>> listMovieMutableLiveData, String order) {

            RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

            Call<MovieJsonResponse> call = service.getMovies(order, API_KEY);

            call.enqueue(new Callback<MovieJsonResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieJsonResponse> call, @NonNull Response<MovieJsonResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            DataWrapper<List<Movie>> dataWrapper = new DataWrapper<>(response.body().getResults(), null);
                            listMovieMutableLiveData.setValue(dataWrapper);
                        }
                    }else{
                        DataWrapper<List<Movie>> dataWrapper = new DataWrapper<>(null, response.message());
                        listMovieMutableLiveData.setValue(dataWrapper);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieJsonResponse> call, @NonNull Throwable error) {
                    DataWrapper<List<Movie>> dataWrapper = new DataWrapper<>(null, error.getMessage());
                    listMovieMutableLiveData.setValue(dataWrapper);
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

    public void loadTrailer(final MutableLiveData<DataWrapper<List<Trailer>>> listTrailerMutableLiveData, int id) {
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<TrailerJsonResponse> call = service.getTrailers(id, API_KEY);

        call.enqueue(new Callback<TrailerJsonResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrailerJsonResponse> call, @NonNull Response<TrailerJsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        DataWrapper<List<Trailer>> dataWrapper = new DataWrapper<>(response.body().getResults(), null);
                        listTrailerMutableLiveData.setValue(dataWrapper);
                    }
                }else{
                    DataWrapper<List<Trailer>> dataWrapper = new DataWrapper<>(null, response.message());
                    listTrailerMutableLiveData.setValue(dataWrapper);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailerJsonResponse> call, @NonNull Throwable error) {
                DataWrapper<List<Trailer>> dataWrapper = new DataWrapper<>(null, error.getMessage());
                listTrailerMutableLiveData.setValue(dataWrapper);
            }
        });

    }

    public void loadReviews(final MutableLiveData<DataWrapper<List<Review>>> listReviewMutableLiveData, int id) {
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<ReviewJsonResponse> call = service.getReviews(id, API_KEY);

        call.enqueue(new Callback<ReviewJsonResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewJsonResponse> call, @NonNull Response<ReviewJsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        DataWrapper<List<Review>> dataWrapper = new DataWrapper<>(response.body().getResults(), null);
                        listReviewMutableLiveData.setValue(dataWrapper);
                    }
                }else{
                    DataWrapper<List<Review>> dataWrapper = new DataWrapper<>(null, response.message());
                    listReviewMutableLiveData.setValue(dataWrapper);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewJsonResponse> call, @NonNull Throwable error) {
                DataWrapper<List<Review>> dataWrapper = new DataWrapper<>(null, error.getMessage());
                listReviewMutableLiveData.setValue(dataWrapper);
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
