package com.willpasco.clickies.adapter;

import com.willpasco.clickies.fragment.FavoriteFragment;
import com.willpasco.clickies.fragment.MoviesFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomeAdapter extends FragmentPagerAdapter {

    public static final int MOVIE_LIST_POSITION = 0;
    public static final int FAVORITE_LIST_POSITION = 1;

    public HomeAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MOVIE_LIST_POSITION:
                return new MoviesFragment();
            case FAVORITE_LIST_POSITION:
                return new FavoriteFragment();
            default:
                return null;
        }
    }

}
