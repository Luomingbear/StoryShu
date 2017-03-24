package com.lzy.imagepicker.loader;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by bear on 2017/3/24.
 */

public class GlideImageLoader implements ImageLoader {
    public GlideImageLoader() {
    }

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide
                .with(activity)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .dontAnimate()
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
