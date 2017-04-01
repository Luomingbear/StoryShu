package com.storyshu.storyshu.mvp.base;

import android.content.Context;

import com.storyshu.storyshu.mvp.view.base.IBaseView;

/**
 * mvp
 * 基本的方法
 * Created by bear on 2017/4/1.
 */

public class IBasePresenter<T extends IBaseView> {
    public Context mContext;
    public T mMvpView;

    public IBasePresenter(Context mContext, T mvpView) {
        this.mContext = mContext;
        this.mMvpView = mvpView;
    }

    /**
     * 销毁释放view和context
     */
    public void distach() {
        mContext = null;
        mMvpView = null;
    }
}
