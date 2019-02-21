package com.willpasco.clickies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;
import com.willpasco.clickies.model.MovieJsonResponse;
import com.willpasco.clickies.service.RetrofitService;
import com.willpasco.clickies.service.ServiceGenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.willpasco.clickies.service.ServiceGenerator.API_KEY;

public class HomeActivity extends AppCompatActivity {

    private static final String POPULAR_SEARCH_TYPE = "popular";
    private static final String TOP_RATED_SEARCH_TYPE = "top_rated";
    private static final int CONNECTION_CHECK_REQUEST_CODE = 4521;

    private MovieRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private JellyToggleButton jellyToggleButton;
    private LinearLayout errorState;
    private LinearLayout noNetWorkState;
    private String searchType = POPULAR_SEARCH_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        onBindView();
        configErrorButton();
        configNoNetworkButton();

        adapter = new MovieRecyclerAdapter();

        jellyToggleButton.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener() {
            @Override
            public void onStateChange(float process, State state, JellyToggleButton jtb) {
                if (state == State.LEFT) {
                    showLoadingState();
                    searchType = POPULAR_SEARCH_TYPE;
                    requestMovies(searchType);
                } else if (state == State.RIGHT) {
                    showLoadingState();
                    searchType = TOP_RATED_SEARCH_TYPE;
                    requestMovies(searchType);
                }
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(HomeActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        requestMovies(searchType);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void configErrorButton() {
        Button retryButton = errorState.findViewById(R.id.button_retry);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingState();
                requestMovies(searchType);
            }
        });
    }

    private void configNoNetworkButton() {
        Button buttonConfig = noNetWorkState.findViewById(R.id.button_config);

        buttonConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingState();
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivityForResult(intent, HomeActivity.CONNECTION_CHECK_REQUEST_CODE);
            }
        });
    }

    private void onBindView() {
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        jellyToggleButton = findViewById(R.id.jellyToggleButton);
        errorState = findViewById(R.id.include_error_state);
        noNetWorkState = findViewById(R.id.include_no_network_state);
    }


    private void requestMovies(String order) {
        if (isNetworkAvailable()) {

            RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

            Call<MovieJsonResponse> call = service.getMovies(order, API_KEY);

            call.enqueue(new Callback<MovieJsonResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieJsonResponse> call, @NonNull Response<MovieJsonResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            recyclerView.scrollToPosition(0);
                            adapter.addAll(response.body().getResults());
                            showContentState();
                        } else {
                            showErrorState();
                        }
                    } else {
                        showErrorState();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieJsonResponse> call, @NonNull Throwable t) {
                    showErrorState();
                }
            });

        } else {
            showNoNetworkState();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONNECTION_CHECK_REQUEST_CODE) {
            if (!isNetworkAvailable()) {
                showNoNetworkState();
                Toast.makeText(HomeActivity.this, R.string.toast_no_internet_connection, Toast.LENGTH_LONG).show();
            } else {
                requestMovies(searchType);
            }
        }
    }


    private void showErrorState() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorState.setVisibility(View.VISIBLE);
        noNetWorkState.setVisibility(View.GONE);
    }


    private void showContentState() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        errorState.setVisibility(View.GONE);
        noNetWorkState.setVisibility(View.GONE);
    }


    private void showLoadingState() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorState.setVisibility(View.GONE);
        noNetWorkState.setVisibility(View.GONE);
    }

    private void showNoNetworkState() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorState.setVisibility(View.GONE);
        noNetWorkState.setVisibility(View.VISIBLE);

    }

}
