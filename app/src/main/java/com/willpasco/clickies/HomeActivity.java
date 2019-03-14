package com.willpasco.clickies;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class HomeActivity extends AppCompatActivity {

    public static final String POPULAR_SEARCH_TYPE = "popular";
    public static final String TOP_RATED_SEARCH_TYPE = "top_rated";

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new HomeAdapter(getSupportFragmentManager()));

    }
}
