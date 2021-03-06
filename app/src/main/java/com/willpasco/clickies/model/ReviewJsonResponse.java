package com.willpasco.clickies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewJsonResponse {

    @SerializedName("results")
    @Expose
    private final List<Review> reviewList = null;

    public List<Review> getResults() {
        return reviewList;
    }


}
