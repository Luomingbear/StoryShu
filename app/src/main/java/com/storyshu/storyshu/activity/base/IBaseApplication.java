package com.storyshu.storyshu.activity.base;

import android.app.Application;

/**
 * 记录是否是夜间模式的数据
 * Created by bear on 2017/2/13.
 */

public class IBaseApplication extends Application {
    private boolean isNightMode = false; //夜间模式

    public boolean isNightMode() {
        return isNightMode;
    }

    public void setNightMode(boolean nightMode) {
        isNightMode = nightMode;
    }
}
