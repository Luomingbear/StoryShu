package com.storyshu.storyshu.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.imagepicker.PickerConfig;
import com.storyshu.storyshu.imagepicker.SImageLoader;
import com.storyshu.storyshu.imagepicker.SImagePicker;

/**
 * Created by bear on 2017/3/23.
 */

public class IApplication extends Application {

    /**
     * 在这里初始化一些东西
     */
    @Override
    public void onCreate() {
        super.onCreate();

        startInitThread();
    }

    /**
     * 开启线程进行数据的初始化
     */
    private void startInitThread() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                initImageLoader();
            }
        });
        thread1.start();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                initImagePicker();
            }
        });
        thread2.start();
    }


    /**
     * 初始化图像加载器
     */
    private void initImageLoader() {
        FadeInBitmapDisplayer fadeInBitmapDisplayer = new FadeInBitmapDisplayer(200, true, false, false); //设置图片渐显，200毫秒

        DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(fadeInBitmapDisplayer)
                .showImageOnLoading(R.drawable.gray_bg).cacheInMemory(true)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(configuration);
    }

    private void initImagePicker() {
        SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
                .setImageLoader(new SImageLoader() {
                    @Override
                    public void bindImage(ImageView imageView, Uri uri, int width, int height) {
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                                .displayImage("file://" + uri.getPath(), imageView, new ImageSize(width, height));
                    }

                    @Override
                    public void bindImage(ImageView imageView, Uri uri) {
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                                .displayImage("file://" + uri.getPath(), imageView);
                    }

                    @Override
                    public ImageView createImageView(Context context) {
                        ImageView view = new ImageView(context);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        view.setLayoutParams(params);
                        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return view;
                    }

                    @Override
                    public ImageView createFakeImageView(Context context) {
                        ImageView view = new ImageView(context);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        view.setLayoutParams(params);
                        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return view;
                    }
                })
                .setToolbaseColor(getResources().getColor(R.color.colorGoldLight))
                .build());
    }
}
