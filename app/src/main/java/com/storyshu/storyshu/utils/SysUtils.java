package com.storyshu.storyshu.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * 获取系统信息的工具
 * Created by bear on 2017/3/16.
 */

public class SysUtils {

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
}
