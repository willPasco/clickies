package com.willpasco.clickies;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

    private MovieRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private JellyToggleButton jellyToggleButton;
    private LinearLayout errorState;
    private String searchType = POPULAR_SEARCH_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        onBindView();
        configErrorButton();

        adapter = new MovieRecyclerAdapter();

        jellyToggleButton.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener() {
            @Override
            public void onStateChange(float process, State state, JellyToggleButton jtb) {
                if (state == State.LEFT) {
                    showLoadingState();
                    searchType = POPULAR_SEARCH_TYPE;
                    retrofitConverter(searchType);
                } else if (state == State.RIGHT) {
                    showLoadingState();
                    searchType = TOP_RATED_SEARCH_TYPE;
                    retrofitConverter(searchType);
                }
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(HomeActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        retrofitConverter(searchType);
    }

    private void configErrorButton() {
        Button retryButton = errorState.findViewById(R.id.button_retry);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingState();
                retrofitConverter(searchType);
            }
        });
    }

    private void onBindView() {
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        jellyToggleButton = findViewById(R.id.jellyToggleButton);
        errorState = findViewById(R.id.include_error_state);
    }


    private void retrofitConverter(String order) {

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

            }
        });
    }

    private void showErrorState() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorState.setVisibility(View.VISIBLE);
    }


    private void showContentState() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        errorState.setVisibility(View.GONE);
    }


    private void showLoadingState() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorState.setVisibility(View.GONE);
    }
}
