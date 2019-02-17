package com.willpasco.clickies.service;

import com.willpasco.clickies.model.MovieJsonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.willpasco.clickies.service.ServiceGenerator.API_KEY;

public interface RetrofitService {


    @GET("/3/movie/{order}")
    Call<MovieJsonResponse> getMovies(@Path("order") String order, @Query("api_key") String key);
}
