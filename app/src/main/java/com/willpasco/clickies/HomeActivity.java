package com.willpasco.clickies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;
import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.viewmodel.MovieViewModel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeActivity extends AppCompatActivity {

    public static final String POPULAR_SEARCH_TYPE = "popular";
    public static final String TOP_RATED_SEARCH_TYPE = "top_rated";
    private static final int CONNECTION_CHECK_REQUEST_CODE = 4521;

    private CoordinatorLayout rootView;
    private MovieRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private JellyToggleButton jellyToggleButton;
    private LinearLayout noContentState;
    private LinearLayout noNetWorkState;
    private String searchType = POPULAR_SEARCH_TYPE;
    private MovieViewModel movieViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        onBindView();

        adapter = new MovieRecyclerAdapter();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        jellyToggleButton.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener() {
            @Override
            public void onStateChange(float process, State state, JellyToggleButton jtb) {
                if (state == State.LEFT) {
                    showLoadingState();
                    searchType = POPULAR_SEARCH_TYPE;
                    refreshData();
                } else if (state == State.RIGHT) {
                    showLoadingState();
                    searchType = TOP_RATED_SEARCH_TYPE;
                    refreshData();
                }
            }
        });

        refreshData();

        GridLayoutManager layoutManager = new GridLayoutManager(HomeActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        MutableLiveData<List<Movie>> listMovieMutableLiveData = movieViewModel.getListMovieLiveData(searchType);
        listMovieMutableLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                showContentState();
                adapter.addAll(movies);

                dismissRefreshing();

                if(adapter.getItemCount() <= 0){
                    showNoContentState();
                }
            }
        });

    }

    private void dismissRefreshing() {
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void refreshData() {
        if(!isNetworkAvailable()){
            dismissRefreshing();
            showNoNetworkState();
        }else{
            movieViewModel.getListMovieLiveData(searchType);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void onBindView() {
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        jellyToggleButton = findViewById(R.id.jellyToggleButton);
        noContentState = findViewById(R.id.include_no_content_state);
        noNetWorkState = findViewById(R.id.include_no_network_state);
        rootView = findViewById(R.id.parent_layout);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONNECTION_CHECK_REQUEST_CODE) {
            if (!isNetworkAvailable()) {
                showNoNetworkState();
            } else {
                Toast.makeText(this, R.string.internet_connected, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showNoContentState() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        noContentState.setVisibility(View.VISIBLE);
    }


    private void showContentState() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        noContentState.setVisibility(View.GONE);
    }


    private void showLoadingState() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        noContentState.setVisibility(View.GONE);
    }

    private void showNoNetworkState() {
        Snackbar.make(recyclerView, R.string.no_network, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.snack_bar_settings_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivityForResult(intent, HomeActivity.CONNECTION_CHECK_REQUEST_CODE);
                    }
                }).setActionTextColor(getResources().getColor(R.color.snack_action_color))
                .show();
    }

}
