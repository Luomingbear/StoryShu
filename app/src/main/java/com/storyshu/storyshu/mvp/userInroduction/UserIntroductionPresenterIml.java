package com.storyshu.storyshu.mvp.userInroduction;

import android.content.Context;

import com.storyshu.storyshu.mvp.base.IBasePresenter;

/**
 * Created by bear on 2017/6/5.
 */

public class UserIntroductionPresenterIml extends IBasePresenter<UserIntroductionView> implements UserIntroductionPresenter {

    public UserIntroductionPresenterIml(Context mContext, UserIntroductionView mvpView) {
        super(mContext, mvpView);
    }


}
