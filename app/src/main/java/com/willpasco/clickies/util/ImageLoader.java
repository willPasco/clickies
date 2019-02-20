package com.willpasco.clickies.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

public class ImageLoader {

    public static void loadImage(String url, ImageView view) {


        Glide.with(view.getContext())
                .load(url)
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(view);
    }
}
