package com.willpasco.clickies.adapter;

import com.willpasco.clickies.fragment.FavoriteFragment;
import com.willpasco.clickies.fragment.MoviesFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomeAdapter extends FragmentPagerAdapter {

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
            case 0:
                return new MoviesFragment();
            case 1:
                return new FavoriteFragment();
            default:
                return null;
        }
    }

}