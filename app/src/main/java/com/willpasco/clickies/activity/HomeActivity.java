package com.willpasco.clickies.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.willpasco.clickies.R;
import com.willpasco.clickies.adapter.HomeAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import static com.willpasco.clickies.adapter.HomeAdapter.FAVORITE_LIST_POSITION;
import static com.willpasco.clickies.adapter.HomeAdapter.MOVIE_LIST_POSITION;

public class HomeActivity extends AppCompatActivity {

    public static final String POPULAR_SEARCH_TYPE = "popular";
    public static final String TOP_RATED_SEARCH_TYPE = "top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ViewPager viewPager = findViewById(R.id.view_pager);
        final BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation_home);

        viewPager.setAdapter(new HomeAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {

                    case FAVORITE_LIST_POSITION:
                        bottomNavigation.setSelectedItemId(R.id.menu_favorite_list);
                        break;

                    case MOVIE_LIST_POSITION:
                        bottomNavigation.setSelectedItemId(R.id.menu_movies_list);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_favorite_list:
                        viewPager.setCurrentItem(FAVORITE_LIST_POSITION);
                        return true;

                    case R.id.menu_movies_list:
                        viewPager.setCurrentItem(MOVIE_LIST_POSITION);
                        return true;

                    default:
                        return false;
                }
            }
        });

    }
}
