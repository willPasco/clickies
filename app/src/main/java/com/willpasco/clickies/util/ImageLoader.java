package com.willpasco.clickies.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageLoader {

    public static void loadImage(String url, ImageView view) {

        Glide.with(view.getContext())
                .load(url)
                .fitCenter()
                .into(view);
    }
}
