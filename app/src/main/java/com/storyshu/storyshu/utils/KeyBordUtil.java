package com.storyshu.storyshu.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 控制键盘的弹出于隐藏的工具
 * Created by bear on 2017/3/31.
 */

public class KeyBordUtil {
    /**
     * 显示键盘
     */
    public static void showKeyboard(final Context context, final View view) {
        /***
         * 由于界面为加载完全而无法弹出软键盘所以延时一段时间弹出键盘
         */
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputMethodManager =
                                       (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputMethodManager.showSoftInput(view, 0);
                           }
                       },
                180);
    }

    /**
     * 隐藏键盘
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
