package com.storyshu.storyshu.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;

import com.lzy.imagepicker.ui.ImageGridActivity;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

/**
 * 基本的activity
 * Created by bear on 2016/12/6.
 */

public class IBaseActivity extends AppCompatActivity {

    public final int REQUEST_CODE_IMAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (ISharePreference.isNightMode(this))
            setTheme(R.style.appTheme_night);
        else
            setTheme(R.style.appTheme_day);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {

        if (ISharePreference.isNightMode(this))
            setTheme(R.style.appTheme_night);
        else
            setTheme(R.style.appTheme_day);

        super.onCreate(savedInstanceState, persistentState);
    }

    public void changDayOrNight() {
        ISharePreference.setIsNightMode(this, !ISharePreference.isNightMode(this));
//        recreateOnResume();
    }

    /**
     * 等待resume执行完毕再执行recreate
     */
    public void recreateOnResume() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                recreate();
            }
        }, 100);
    }

    /**
     * 跳转到图片选择的页面
     */
    public void intent2ImagePickActivity() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    /**
     * 页面跳转
     *
     * @param cls
     */
    public void intentTo(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);

    }

    public void intentWithFlag(Class<?> cls, int flag) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.setFlags(flag);
        startActivity(intent);
        //淡入淡出
    }

    public void intentWithParcelable(Class<?> cls, String name, Parcelable parcelable) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtra(name, parcelable);
        startActivity(intent);

    }

    public void intentWithParcelable(Class<?> cls, ActivityOptionsCompat options, String name, Parcelable parcelable) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtra(name, parcelable);
        startActivity(intent, options.toBundle());
    }

    public void intentWithBundle(Class<?> cls, Bundle options) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (options != null) {
            startActivityForResult(intent, -1, options);
        } else {
            startActivityForResult(intent, -1);
        }
    }
}


