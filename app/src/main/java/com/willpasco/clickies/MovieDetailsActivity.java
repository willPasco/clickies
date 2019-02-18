package com.willpasco.clickies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.willpasco.clickies.model.Movie;

import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

    }
}
