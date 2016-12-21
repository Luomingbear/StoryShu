package com.storyshu.storyshu.widget.story;

import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * 获取屏幕可以显示卡片数量的工具类
 * Created by bear on 2016/12/4.
 */

public class CardNumUtil {
    private static final String TAG = "CardNumUtil";

    public CardNumUtil() {

    }

    public static int getMaxCardNumInScreen(WindowManager manager, View card) {
        int max;
        Display display = manager.getDefaultDisplay();

        float screenWidth = display.getWidth();
//        float screenHeight = display.getHeight();
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        card.measure(width, height);

        width = card.getMeasuredWidth();
//        height = card.getHeight();
//
//        float minSize = Math.min(width, height);
//        if (screenWidth < screenHeight) {
//
//        } else {
//        }
        max = (int) (screenWidth / width) + 1;
        Log.e(TAG, "getMaxCardNumInScreen: maxNum:" + max);
        return max;
    }
}
