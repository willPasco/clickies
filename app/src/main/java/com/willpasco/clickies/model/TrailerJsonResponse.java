package com.willpasco.clickies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerJsonResponse {

    @SerializedName("results")
    @Expose
    private final List<Trailer> trailerList = null;

    public List<Trailer> getResults() {
        return trailerList;
    }


}
