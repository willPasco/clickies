package com.willpasco.clickies;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;
import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.service.DataWrapper;
import com.willpasco.clickies.viewmodel.MovieViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.willpasco.clickies.HomeActivity.POPULAR_SEARCH_TYPE;
import static com.willpasco.clickies.HomeActivity.TOP_RATED_SEARCH_TYPE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    private static final int CONNECTION_CHECK_REQUEST_CODE = 4521;

    private MovieRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private JellyToggleButton jellyToggleButton;
    private LinearLayout noContentState;
    private String searchType = POPULAR_SEARCH_TYPE;
    private MovieViewModel movieViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;


    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        onBindView(view);

        adapter = new MovieRecyclerAdapter();
        refreshData();

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

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        movieViewModel.getListMovieLiveData().observe(this, new Observer<DataWrapper<List<Movie>>>() {
            @Override
            public void onChanged(DataWrapper<List<Movie>> dataWrapper) {
                if(!dataWrapper.hasError()) {
                    showContentState();
                    adapter.addAll(dataWrapper.getData());

                    dismissRefreshing();

                    if (adapter.getItemCount() <= 0) {
                        showNoContentState();
                    }
                }else{
                    dismissRefreshing();
                    showNoContentState();
                }
            }
        });
        movieViewModel.fetchData(searchType);

    }

    private void dismissRefreshing() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void refreshData() {
        if (noInternetConnection()) {
            dismissRefreshing();
            showNoNetworkState();
        } else {
            movieViewModel.fetchData(searchType);
        }
    }

    private boolean noInternetConnection() {
        if (getContext() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork == null || !activeNetwork.isConnectedOrConnecting();
        } else {
            return true;
        }
    }

    private void onBindView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        jellyToggleButton = view.findViewById(R.id.jellyToggleButton);
        noContentState = view.findViewById(R.id.include_no_content_state);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONNECTION_CHECK_REQUEST_CODE) {
            refreshData();
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
                        startActivityForResult(intent, CONNECTION_CHECK_REQUEST_CODE);
                    }
                }).setActionTextColor(getResources().getColor(R.color.snack_action_color))
                .show();
    }

}
