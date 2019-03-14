package com.willpasco.clickies.service;

import com.willpasco.clickies.model.MovieJsonResponse;
import com.willpasco.clickies.model.TrailerJsonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {


    @GET("/3/movie/{order}")
    Call<MovieJsonResponse> getMovies(@Path("order") String order, @Query("api_key") String key);

    @GET("/3/movie/{id}/videos")
    Call<TrailerJsonResponse> getTrailer(@Path ("id") int id, @Query("api_key") String apiKey);
}
