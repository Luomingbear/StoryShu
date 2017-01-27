package com.storyshu.storyshu.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;

/**
 * 基本的activity
 * Created by bear on 2016/12/6.
 */

public class IBaseActivity extends FragmentActivity {
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


