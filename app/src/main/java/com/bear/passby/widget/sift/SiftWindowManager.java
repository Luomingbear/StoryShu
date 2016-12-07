package com.bear.passby.widget.sift;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

/**
 * 筛选栏管家
 * Created by bear on 2016/12/7.
 */

public class SiftWindowManager {
    private static final String TAG = "SiftWindowManager";
    private static SiftWindowManager instance;
    private SiftWindow mSiftWindow;
    private boolean isShowing = false;

    public static SiftWindowManager getInstance() {
        if (instance == null) {
            synchronized (SiftWindowManager.class) {
                if (instance == null)
                    instance = new SiftWindowManager();
            }
        }
        return instance;
    }

    protected SiftWindowManager() {

    }

    /**
     * 显示筛选栏
     *
     * @param context
     * @param view    在哪个view的底下显示
     */
    public void showSift(Context context, View view) {
//        Log.i(TAG, "showSift: mSift is null:" + (mSiftWindow == null));
//        Log.i(TAG, "showSift: isShow:" + isShowing);
//        if (mSiftWindow != null || isShowing) {
//
////            if (mSiftWindow.isShowing()) {
////                mSiftWindow.dismiss();
////                isShowing = false;
////            }
//
//            mSiftWindow = null;
//            isShowing = false;
//            return;
//        }
        mSiftWindow = new SiftWindow(context);
        mSiftWindow.showAsDropDown(view, 0, 0);
        isShowing = true;
        mSiftWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                mSiftWindow = null;
//                isShowing = false;
            }
        });
    }
}
