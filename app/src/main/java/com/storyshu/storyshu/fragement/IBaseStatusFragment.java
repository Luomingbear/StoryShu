package com.storyshu.storyshu.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storyshu.storyshu.R;

/**
 * 基本的fragment，可以设置状态栏的颜色
 * Created by bear on 2017/3/21.
 */

public abstract class IBaseStatusFragment extends Fragment {
    public View mRootView; //根布局
    private View mFakeStatusBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getLayoutRes() != 0)
            mRootView = inflater.inflate(getLayoutRes(), container, false);
        initView(savedInstanceState);

        initData();

        initEvents();
        return mRootView;
    }

    /**
     * 设置布局id
     *
     * @return
     */
    public abstract int getLayoutRes();

    public abstract void initView(@Nullable Bundle savedInstanceState);

    public abstract void initData();

    public abstract void initEvents();

    /**
     * 设置状态栏背景
     *
     * @param colorRes
     */
    public void setStatusBackgroundColor(int colorRes) {
        mFakeStatusBar = mRootView.findViewById(R.id.fake_statusbar_view);
        mFakeStatusBar.setBackgroundColor(getResources().getColor(colorRes));
    }

    /**
     * 页面跳转
     *
     * @param cls
     */
    public void intentTo(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        startActivity(intent);

    }

    public void intentWithFlag(Class<?> cls, int flag) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        intent.setFlags(flag);
        startActivity(intent);
        //淡入淡出
    }

    public void intentWithParcelable(Class<?> cls, String name, Parcelable parcelable) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        intent.putExtra(name, parcelable);
        startActivity(intent);

    }
}
