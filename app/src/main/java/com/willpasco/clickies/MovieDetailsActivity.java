package com.willpasco.clickies;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.model.Trailer;
import com.willpasco.clickies.service.DataWrapper;
import com.willpasco.clickies.util.ImageLoader;
import com.willpasco.clickies.viewmodel.MovieViewModel;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private MovieViewModel viewModel;
    private ImageView favoriteIcon;
    private RecyclerView trailerRecyclerView;
    private TrailerRecyclerAdapter trailerAdapter;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        onBindView();

        toolbar.setTitle(getString(R.string.toolbar_details_title));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();

        trailerAdapter = new TrailerRecyclerAdapter();
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        trailerRecyclerView.setAdapter(trailerAdapter);

        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(MOVIE_EXTRA_KEY)) {
            final Movie model = intent.getParcelableExtra(MOVIE_EXTRA_KEY);
            movieTitle.setText(model.getTitle());
            movieSynopsis.setText(model.getOverview());
            ImageLoader.loadImageCenterInside(BASE_IMAGE_PATH + model.getPosterPath(), movieImagePoster);
            movieDate.setText(formatDate(model.getReleaseDate()));
            voteRated.setProgressValue(model.getVoteAverage());

            favoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isFavorite){
                        viewModel.setMovieUnFavorite(model);
                        isFavorite = false;
                        changeIcon();
                    }else{
                        viewModel.setMovieFavorite(model);
                        isFavorite = true;
                        changeIcon();
                    }
                }
            });

            new CheckFavoriteAsyncTask().execute(model.getId());

            viewModel.getListTrailerMutableLiveData().observe(this, new Observer<DataWrapper<List<Trailer>>>() {
                @Override
                public void onChanged(DataWrapper<List<Trailer>> dataWrapper) {
                    if(!dataWrapper.hasError()){
                        trailerAdapter.addAll(dataWrapper.getData());
                    }else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            loadTrailers(model.getId());
        }

    }

    private void loadTrailers(int id) {
        viewModel.loadTrailers(id);
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
        favoriteIcon = findViewById(R.id.image_view_favorite);
        trailerRecyclerView = findViewById(R.id.recycler_view_trailer);
    }

    private class CheckFavoriteAsyncTask extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... integers) {
            return viewModel.isFavoriteMovie(integers[0]);
        }

        @Override
        protected void onPostExecute(Boolean favoriteMovie) {
            isFavorite = favoriteMovie;
            changeIcon();
        }
    }

    private void changeIcon() {
        if(isFavorite){
            ImageLoader.loadImage(R.drawable.ic_favorite, favoriteIcon);
        }else{
            ImageLoader.loadImage(R.drawable.ic_unfavorite, favoriteIcon);
        }
    }
}
