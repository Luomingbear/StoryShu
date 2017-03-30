package com.storyshu.storyshu.utils;

import android.app.Application;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.loader.GlideImageLoader;

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
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                initImagePicker();
            }
        });
        thread2.start();
    }

    /**
     * 初始化图片选择器
     */
    private void initImagePicker() {

        ImagePicker.getInstance()
                .setImageLoader(new GlideImageLoader())   //设置图片加载器
                .setFocusWidth(SysUtils.getScreenWidth(getApplicationContext())) //裁剪框的大小
                .setFocusHeight(SysUtils.getScreenWidth(getApplicationContext()));
//        imagePicker.setShowCamera(true);  //显示拍照按钮
//        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
//        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
//        imagePicker.setSelectLimit(9);    //选中数量限制
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
//        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }
}
