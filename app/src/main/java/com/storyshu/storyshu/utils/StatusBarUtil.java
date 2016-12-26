package com.storyshu.storyshu.utils;

import android.content.Context;

/**
 * 状态栏高度的工具
 * Created by bear on 2016/12/26.
 */

public class StatusBarUtil {
    /**
     * 获取通知栏的高度
     *
     * @return 状态栏的高度
     */
    public static int getHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
