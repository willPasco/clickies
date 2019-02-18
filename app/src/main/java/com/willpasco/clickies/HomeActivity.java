package com.willpasco.clickies;

import android.os.Bundle;
import android.util.Log;

import com.willpasco.clickies.model.MovieJsonResponse;
import com.willpasco.clickies.service.RetrofitService;
import com.willpasco.clickies.service.ServiceGenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.willpasco.clickies.service.ServiceGenerator.API_KEY;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private MovieRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        adapter = new MovieRecyclerAdapter();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(HomeActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        retrofitConverter("popular");
    }


    public void retrofitConverter(String order) {

        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<MovieJsonResponse> call = service.getMovies(order, API_KEY);

        call.enqueue(new Callback<MovieJsonResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieJsonResponse> call, @NonNull Response<MovieJsonResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body() != null){
                        adapter.addAll(response.body().getResults());
                    }
                    Log.i(TAG, response.body().getResults() + "");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieJsonResponse> call, @NonNull Throwable t) {

            }
        });

    }
}
