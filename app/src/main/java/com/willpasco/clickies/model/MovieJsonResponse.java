package com.willpasco.clickies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieJsonResponse {

    @SerializedName("results")
    @Expose
    private final List<Movie> movieList = null;

    public List<Movie> getResults() {
        return movieList;
    }


}
