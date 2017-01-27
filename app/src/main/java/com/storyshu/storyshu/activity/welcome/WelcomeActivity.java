package com.storyshu.storyshu.activity.welcome;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.activity.login.LoginActivity;
import com.storyshu.storyshu.activity.story.StoryMapActivity;
import com.storyshu.storyshu.info.UserInfo;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 欢迎页
 * 定时3秒之后自动跳转到地图页面
 * Created by bear on 2016/12/20.
 */

public class WelcomeActivity extends IBaseActivity {
    private int mWaitTime = 3200; //自动跳转等待的时间 单位毫秒
    private int mAnimationTime = 1600; //动画执行时间，单位毫秒
    private Timer mTimer;//定时器
    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_layout);
        initView();
        initImageLoader();
//        initUSerData();
        initTimer();
    }

    private void initView() {

        startLogoAnimation();

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
     * 初始化游客身份
     */
    private void initUSerData() {
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar("https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=2b7f34583da85edfee81f671283d6246/f703738da97739122455d869f1198618367ae243.jpg");
        userInfo.setNickname("翻船");
        ISharePreference.saveUserData(this, userInfo);
    }

    /**
     * 播放logo动画
     */
    private void startLogoAnimation() {

        //logo图片动画
        View dishu = findViewById(R.id.welcome_dishu);
        Interpolator dl = new DecelerateInterpolator();
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(dishu, "translationY",
                getResources().getDimension(R.dimen.image_normal), getResources().getDimension(R.dimen.image_normal) / 3.5f);
        translationAnimator.setInterpolator(dl);
        translationAnimator.setDuration(mAnimationTime);
        translationAnimator.start();

        //logo文字动画

        View name = findViewById(R.id.welcome_logo_text);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(mAnimationTime + 500);
        alphaAnimation.setInterpolator(dl);
        name.setAnimation(alphaAnimation);
        alphaAnimation.start();

    }


    /**
     * 初始化图像加载器
     */
    private void initImageLoader() {
        FadeInBitmapDisplayer fadeInBitmapDisplayer = new FadeInBitmapDisplayer(200, true, false, false); //设置图片渐显，200毫秒

        DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(fadeInBitmapDisplayer)
                .showImageOnLoading(R.drawable.gray_bg).cacheInMemory(true)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options).build();

        ImageLoader.getInstance().init(configuration);
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
        UserInfo userInfo = ISharePreference.getUserData(this);
        if (userInfo.getUserId() == UserInfo.Visitor)
            intentWithFlag(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        else
            intentWithFlag(StoryMapActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
}
