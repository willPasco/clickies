package com.willpasco.clickies;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.util.ImageLoader;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import rjsv.circularview.CircleView;

import static com.willpasco.clickies.MovieRecyclerAdapter.BASE_IMAGE_PATH;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE_EXTRA_KEY = "movie";
    private TextView movieTitle;
    private TextView movieSynopsis;
    private ImageView movieImagePoster;
    private TextView movieDate;
    private Toolbar toolbar;
    private CircleView voteRated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        onBindView();

        toolbar.setTitle(getString(R.string.toolbar_details_title));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(MOVIE_EXTRA_KEY)) {
            Movie model = intent.getParcelableExtra(MOVIE_EXTRA_KEY);
            movieTitle.setText(model.getTitle());
            movieSynopsis.setText(model.getOverview());
            ImageLoader.loadImageCenterInside(BASE_IMAGE_PATH + model.getPosterPath(), movieImagePoster);
            movieDate.setText(formatDate(model.getReleaseDate()));
            voteRated.setProgressValue(model.getVoteAverage());
        }

    }

    private String formatDate(String date) {
        String[] strings = date.split("-");
        String day = strings[2];
        String month = strings[1];
        String year = strings[0];
        return day + "/" + month + "/" + year;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void onBindView() {
        movieTitle = findViewById(R.id.text_view_movie_title);
        movieSynopsis = findViewById(R.id.text_view_movie_synopsis);
        movieImagePoster = findViewById(R.id.image_view_movie_poster);
        movieDate = findViewById(R.id.text_view_movie_date);
        toolbar = findViewById(R.id.toolbar);
        voteRated = findViewById(R.id.circle_view_vote_rated);
    }
}
