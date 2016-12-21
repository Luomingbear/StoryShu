package com.storyshu.storyshu.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具
 * Created by bear on 2016/12/3.
 */

public class ToastUtil {
    public static void Show(Context context, String s) {
        if (context == null)
            return;
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void Show(Context context, int resId) {
        if (context == null)
            return;
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
