package com.storyshu.storyshu.fragement;

import android.support.v4.app.Fragment;
import android.view.View;

import com.storyshu.storyshu.R;

/**
 * 基本的fragment，可以设置状态栏的颜色
 * Created by bear on 2017/3/21.
 */

public class IBaseStatusFragment extends Fragment {
    public View mRootView; //根布局
    private View mFakeStatusBar;

    /**
     * 设置状态栏背景
     *
     * @param colorRes
     */
    public void setStatusBackgroundColor(int colorRes) {
        mFakeStatusBar = mRootView.findViewById(R.id.fake_statusbar_view);
        mFakeStatusBar.setBackgroundColor(getResources().getColor(colorRes));
    }
}
