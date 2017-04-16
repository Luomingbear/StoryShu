package com.storyshu.storyshu.model;

import android.content.Context;

import com.storyshu.storyshu.data.DateBaseHelperIml;
import com.storyshu.storyshu.info.BaseUserInfo;

/**
 * mvp模式
 * 用户信息数据管理
 * Created by bear on 2017/3/20.
 */

public class UserModel {
    private Context mAppContext;
    private OnUserInfoGetListener onUserInfoGetListener;

    public void setOnUserInfoGetListener(OnUserInfoGetListener onUserInfoGetListener) {
        this.onUserInfoGetListener = onUserInfoGetListener;
    }

    public interface OnUserInfoGetListener {
        void onSucceed(BaseUserInfo userInfo);

        void onFailed();
    }

    public UserModel(Context mAppContext) {
        this.mAppContext = mAppContext;
    }

    /**
     * 获取用户的信息
     *
     * @param userId
     */
    public void getUserInfo(int userId, OnUserInfoGetListener onUserInfoGetListener) {
        this.onUserInfoGetListener = onUserInfoGetListener;
        BaseUserInfo userInfo;

        DateBaseHelperIml dateBaseHelperIml = new DateBaseHelperIml(mAppContext);
        userInfo = dateBaseHelperIml.getUserInfo(userId);

        // TODO: 2017/3/20 获取服务器信息得到用户的数据
        if (onUserInfoGetListener != null)
            onUserInfoGetListener.onSucceed(userInfo);
    }
}
