package com.storyshu.storyshu.widget.sift;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import com.storyshu.storyshu.R;

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
    public SiftWindowManager showSift(Context context, View view) {
        if (isShowing) {
            this.dismissSift();
        } else {
            mSiftWindow = new SiftWindow(context);
            int margin = (int) context.getResources().getDimension(R.dimen.margin_small);
            mSiftWindow.showAsDropDown(view, -mSiftWindow.getWidth() + view.getWidth() - margin, 0);
            mSiftWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (onSiftDismissListener != null)
                        onSiftDismissListener.onDismiss();
                }
            });
        }
        isShowing = !isShowing;
        return this;
    }

    /**
     * 隐藏筛选栏
     */
    public void dismissSift() {
        if (mSiftWindow == null)
            return;
        if (mSiftWindow.isShowing())
            mSiftWindow.dismiss();
    }

    /**
     * 是否处于显示状态
     *
     * @return
     */
    public boolean isShowing() {
        if (mSiftWindow == null)
            return false;
        return mSiftWindow.isShowing();
    }


    private OnSiftDismissListener onSiftDismissListener;

    public void setOnSiftDismissListener(OnSiftDismissListener onSiftDismissListener) {
        this.onSiftDismissListener = onSiftDismissListener;
    }

    /**
     * 筛选栏显示监听函数
     */
    public interface OnSiftDismissListener {
        /**
         * 筛选栏隐藏时
         */
        void onDismiss();
    }
}
