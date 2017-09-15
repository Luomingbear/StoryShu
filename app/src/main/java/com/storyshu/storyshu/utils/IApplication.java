package com.storyshu.storyshu.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.loader.GlideImageLoader;

import java.util.Iterator;
import java.util.List;

/**
 * 自定义的Application
 * Created by bear on 2017/3/23.
 */

public class IApplication extends Application {
    private static final String TAG = "IApplication";
    private static IApplication instance;

    public static IApplication getInstance() {
        return instance;
    }

    /**
     * 在这里初始化一些东西
     */
    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        startInitThread();
    }

    /**
     * 开启线程进行数据的初始化
     */
    private void startInitThread() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                initImagePicker();
            }
        });
        thread1.start();


        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                initHXSdk();
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

    /**
     * 初始化环信sdk
     */
    private void initHXSdk() {
        int pid = android.os.Process.myPid();

        String processAppName = getAppName(pid);

        //避免二次初始化
        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);


        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
