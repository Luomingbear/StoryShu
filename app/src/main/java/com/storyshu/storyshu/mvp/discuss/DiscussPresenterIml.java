package com.storyshu.storyshu.mvp.discuss;

import android.content.Context;

import com.storyshu.storyshu.mvp.base.IBasePresenter;

/**
 * mvp
 * 讨论的控制
 * Created by bear on 2017/5/24.
 */

public class DiscussPresenterIml extends IBasePresenter<DisscussView> implements DiscussPresenter {
    public DiscussPresenterIml(Context mContext, DisscussView mvpView) {
        super(mContext, mvpView);
    }
}
