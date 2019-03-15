package com.willpasco.clickies.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

public class ImageLoader {

    public static void loadImageCenterCrop(String url, ImageView view) {
        Glide.with(view.getContext())
                .load(url)
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(view);
    }

    public static void loadImageCenterInside(String url, ImageView view) {
        Glide.with(view.getContext())
                .load(url)
                .transform(new CenterInside(), new RoundedCorners(20))
                .into(view);
    }

    public static void loadImage(int resId, ImageView view) {
        Glide.with(view.getContext())
                .load(resId)
                .into(view);
    }
}
