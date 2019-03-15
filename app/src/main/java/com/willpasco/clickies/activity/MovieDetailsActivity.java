package com.willpasco.clickies.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.willpasco.clickies.R;
import com.willpasco.clickies.adapter.ReviewRecyclerAdapter;
import com.willpasco.clickies.adapter.TrailerRecyclerAdapter;
import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.model.Review;
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

import static com.willpasco.clickies.adapter.MovieRecyclerAdapter.BASE_IMAGE_PATH;
import static com.willpasco.clickies.fragment.MoviesFragment.CONNECTION_CHECK_REQUEST_CODE;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE_EXTRA_KEY = "movie";
    private boolean isFavorite = false;

    private View rootView;
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
    private ProgressBar trailerProgressBar;
    private LinearLayout trailerErrorState;

    private ReviewRecyclerAdapter reviewAdapter;
    private RecyclerView reviewRecyclerView;
    private ProgressBar reviewProgressBar;
    private LinearLayout reviewErrorState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        onBindView();

        if (noInternetConnection()) {
            showNoNetworkState();
        }

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
            final Movie model = intent.getParcelableExtra(MOVIE_EXTRA_KEY);

            populateMainContent(model);

            configTrailerContent(model);

            configReviewContent(model);

        }

    }

    private void configReviewContent(Movie model) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        reviewAdapter = new ReviewRecyclerAdapter();
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewRecyclerView.setAdapter(reviewAdapter);

        viewModel.loadReviews(model.getId());
        configReviewErrorButton(model.getId());

        viewModel.getListReviewMutableLiveData().observe(this, new Observer<DataWrapper<List<Review>>>() {
            @Override
            public void onChanged(DataWrapper<List<Review>> dataWrapper) {
                if (dataWrapper.checkError()) {
                    reviewAdapter.addAll(dataWrapper.getData());
                    showReviewContentState();
                    if (trailerAdapter.getItemCount() <= 0) {
                        showReviewErrorState();
                    }
                } else {
                    showReviewErrorState();
                }
            }
        });

    }

    private void configTrailerContent(Movie model) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        trailerAdapter = new TrailerRecyclerAdapter();
        trailerRecyclerView.setLayoutManager(layoutManager);
        trailerRecyclerView.setAdapter(trailerAdapter);

        viewModel.loadTrailers(model.getId());
        configTrailerErrorButton(model.getId());

        viewModel.getListTrailerMutableLiveData().observe(this, new Observer<DataWrapper<List<Trailer>>>() {
            @Override
            public void onChanged(DataWrapper<List<Trailer>> dataWrapper) {
                if (dataWrapper.checkError()) {
                    trailerAdapter.addAll(dataWrapper.getData());
                    showTrailerContentState();
                    if (trailerAdapter.getItemCount() <= 0) {
                        showTrailerErrorState();
                    }
                } else {
                    showTrailerErrorState();
                }
            }
        });
    }

    private void populateMainContent(final Movie model) {
        movieTitle.setText(model.getTitle());
        movieSynopsis.setText(model.getOverview());
        ImageLoader.loadImageCenterInside(BASE_IMAGE_PATH + model.getPosterPath(), movieImagePoster);
        movieDate.setText(formatDate(model.getReleaseDate()));
        voteRated.setProgressValue(model.getVoteAverage());

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    viewModel.setMovieUnFavorite(model);
                    isFavorite = false;
                    changeIcon();
                } else {
                    viewModel.setMovieFavorite(model);
                    isFavorite = true;
                    changeIcon();
                }
            }
        });

        new CheckFavoriteAsyncTask().execute(model.getId());
    }

    private void configTrailerErrorButton(final int id) {
        Button trailerErrorButton = trailerErrorState.findViewById(R.id.button_retry);
        trailerErrorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noInternetConnection()) {
                    showNoNetworkState();
                } else {
                    showTrailerLoadingState();
                    viewModel.loadTrailers(id);
                }
            }
        });
    }

    private void onBindView() {
        rootView = findViewById(R.id.root_layout);
        movieTitle = findViewById(R.id.text_view_movie_title);
        movieSynopsis = findViewById(R.id.text_view_movie_synopsis);
        movieImagePoster = findViewById(R.id.image_view_movie_poster);
        movieDate = findViewById(R.id.text_view_movie_date);
        toolbar = findViewById(R.id.toolbar);
        voteRated = findViewById(R.id.circle_view_vote_rated);
        favoriteIcon = findViewById(R.id.image_view_favorite);

        trailerRecyclerView = findViewById(R.id.recycler_view_trailer);
        trailerProgressBar = findViewById(R.id.progress_bar_trailer);
        trailerErrorState = findViewById(R.id.include_trailer_error_state);

        reviewRecyclerView = findViewById(R.id.recycler_view_review);
        reviewProgressBar = findViewById(R.id.progress_bar_review);
        reviewErrorState = findViewById(R.id.include_review_error_state);
    }

    private void showTrailerLoadingState() {
        trailerProgressBar.setVisibility(View.VISIBLE);
        trailerRecyclerView.setVisibility(View.GONE);
        trailerErrorState.setVisibility(View.GONE);
    }

    private void showTrailerErrorState() {
        trailerProgressBar.setVisibility(View.GONE);
        trailerRecyclerView.setVisibility(View.GONE);
        trailerErrorState.setVisibility(View.VISIBLE);
    }

    private void showTrailerContentState() {
        trailerProgressBar.setVisibility(View.GONE);
        trailerRecyclerView.setVisibility(View.VISIBLE);
        trailerErrorState.setVisibility(View.GONE);
    }

    private void configReviewErrorButton(final int id) {
        Button reviewErrorButton = reviewErrorState.findViewById(R.id.button_retry);
        reviewErrorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noInternetConnection()) {
                    showNoNetworkState();
                } else {
                    showReviewLoadingState();
                    viewModel.loadReviews(id);
                }
            }
        });
    }

    private void showReviewLoadingState() {
        reviewProgressBar.setVisibility(View.VISIBLE);
        reviewRecyclerView.setVisibility(View.GONE);
        reviewErrorState.setVisibility(View.GONE);
    }

    private void showReviewErrorState() {
        reviewProgressBar.setVisibility(View.GONE);
        reviewRecyclerView.setVisibility(View.GONE);
        reviewErrorState.setVisibility(View.VISIBLE);
    }

    private void showReviewContentState() {
        reviewProgressBar.setVisibility(View.GONE);
        reviewRecyclerView.setVisibility(View.VISIBLE);
        reviewErrorState.setVisibility(View.GONE);
    }

    private void showNoNetworkState() {
        Snackbar.make(rootView, R.string.no_network, Snackbar.LENGTH_LONG)
                .setAction(R.string.snack_bar_settings_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivityForResult(intent, CONNECTION_CHECK_REQUEST_CODE);
                    }
                }).setActionTextColor(getResources().getColor(R.color.snack_action_color))
                .show();
    }

    private boolean noInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork == null || !activeNetwork.isConnectedOrConnecting();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONNECTION_CHECK_REQUEST_CODE) {
            if (noInternetConnection()) {
                showNoNetworkState();
            }
        }
    }

    private void changeIcon() {
        if (isFavorite) {
            ImageLoader.loadImage(R.drawable.ic_favorite, favoriteIcon);
        } else {
            ImageLoader.loadImage(R.drawable.ic_unfavorite, favoriteIcon);
        }
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
}
