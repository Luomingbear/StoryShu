package com.storyshu.storyshu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.activity.login.LoginActivity;
import com.storyshu.storyshu.bean.LauncherResponseBean;
import com.storyshu.storyshu.info.BaseUserInfo;
import com.storyshu.storyshu.utils.FileUtil;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.net.RetrofitManager;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.text.StorkTextView;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 欢迎页
 * 定时3秒之后自动跳转到地图页面
 * Created by bear on 2016/12/20.
 */

public class WelcomeActivity extends IBaseActivity {
    private ImageView AdImage; //广告页
    private StorkTextView Describe; //广告描述
    private int mWaitTime = 3200; //自动跳转等待的时间 单位毫秒
    private int mAnimationTime = 1600; //动画执行时间，单位毫秒
    private Timer mTimer;//定时器

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_layout);

        initView();

        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        //状态栏
        StatusBarUtils.setTranslucentForImageView(WelcomeActivity.this, null);

        AdImage = (ImageView) findViewById(R.id.ad_img);

        Describe = (StorkTextView) findViewById(R.id.describe);

        View skip = findViewById(R.id.welcome_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer != null)
                    mTimer.cancel();
                //跳转到地图界面
                intent2Class();
            }
        });
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //将自定义地图的文件复制到内存卡
        String outPath = getFilesDir().getAbsolutePath();
//        Log.i(TAG, "initEvent: outPath：" + outPath);
        outPath = outPath + File.separator + NameUtil.MAP_STYLE;
        try {
            File file = new File(outPath);
            if (!file.exists())
                FileUtil.copyBigDataToSD(this, NameUtil.MAP_STYLE, outPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getLaunchImg();
    }


    /**
     * 获取服务器发送的启动图片
     */
    public void getLaunchImg() {
        Call<LauncherResponseBean> call = RetrofitManager.getInstance().getService().getLauncher();
        call.enqueue(new Callback<LauncherResponseBean>() {
            @Override
            public void onResponse(Call<LauncherResponseBean> call, Response<LauncherResponseBean> response) {
                Glide.with(WelcomeActivity.this).load(response.body().getUrl()).into(AdImage);
                Describe.setText(response.body().getDescribe());

                initTimer();

            }

            @Override
            public void onFailure(Call<LauncherResponseBean> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
                initTimer();

            }
        });

    }

    /**
     * 初始化游客身份
     */
    private void initUSerData() {
        BaseUserInfo userInfo = new BaseUserInfo();
        userInfo.setAvatar("https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=2b7f34583da85edfee81f671283d6246/f703738da97739122455d869f1198618367ae243.jpg");
        userInfo.setNickname("游客");
        ISharePreference.saveUserId(this, -1);
    }

    /**
     * 播放logo动画
     */
    private void startLogoAnimation() {

    }

    /**
     * 设置自动跳转到地图界面
     */
    private void initTimer() {
        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                intent2Class();
            }
        };
        mTimer.schedule(timerTask, mWaitTime);
    }

    /**
     * 跳转界面
     * 如果用户已经登录则跳转到地图界面
     * 没有登录则跳转到登录界面
     */
    private void intent2Class() {
        if (ISharePreference.getUserId(this) == -1)
            intentWithFlag(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        else
            intentWithFlag(MainActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
}