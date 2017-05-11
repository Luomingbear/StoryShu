package com.storyshu.storyshu.model;

import android.content.Context;
import android.util.Log;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.bean.RegisterResponseBean;
import com.storyshu.storyshu.bean.UserIdBean;
import com.storyshu.storyshu.bean.UserLoginResponseBean;
import com.storyshu.storyshu.info.BaseUserInfo;
import com.storyshu.storyshu.info.LoginInfo;
import com.storyshu.storyshu.info.RegisterUserInfo;
import com.storyshu.storyshu.utils.net.CodeUtil;
import com.storyshu.storyshu.utils.net.QiniuUploadManager;
import com.storyshu.storyshu.utils.net.RetrofitManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * mvp模式
 * 用户信息数据管理
 * Created by bear on 2017/3/20.
 */

public class UserModel {
    private static final String TAG = "UserModel";
    private Context mAppContext;
    private OnUserInfoGetListener onUserInfoGetListener;

    public void setOnUserInfoGetListener(OnUserInfoGetListener onUserInfoGetListener) {
        this.onUserInfoGetListener = onUserInfoGetListener;
    }

    public interface OnUserInfoGetListener {
        void onSucceed(BaseUserInfo userInfo);

        void onFailed(String error);
    }

    public UserModel(Context mAppContext) {
        this.mAppContext = mAppContext;
    }

    /**
     * 注册账号
     *
     * @param userInfo
     */
    public void register(final RegisterUserInfo userInfo) {

        /**
         * 上传头像并获取网络地址
         */
        QiniuUploadManager qiniuUploadManager = new QiniuUploadManager();
        qiniuUploadManager.uploadFile(userInfo.getAvatar());
        qiniuUploadManager.setQiniuUploadInterface(new QiniuUploadManager.QiniuUploadInterface() {
            @Override
            public void onSucceed(String fileNetPath) {
                userInfo.setAvatar(fileNetPath);

                //开始注册账户
                uploadUser(userInfo);
            }

            @Override
            public void onFailed() {

            }
        });
    }

    /**
     * 注册账户
     *
     * @param userInfo
     */
    private void uploadUser(RegisterUserInfo userInfo) {

        final Call<RegisterResponseBean> registerCall = RetrofitManager.getInstance().getService().register(userInfo);
        registerCall.enqueue(new Callback<RegisterResponseBean>() {
            @Override
            public void onResponse(Call<RegisterResponseBean> call, Response<RegisterResponseBean> response) {
                if (response.body().getCode() == CodeUtil.Succeed) {
                    if (onUserInfoGetListener != null)
                        onUserInfoGetListener.onSucceed(response.body().getData());
                } else {
                    if (onUserInfoGetListener != null)
                        onUserInfoGetListener.onFailed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseBean> call, Throwable t) {
                if (onUserInfoGetListener != null) {
                    onUserInfoGetListener.onFailed(mAppContext.getString(R.string.register_error));
                }
            }
        });

    }

    /**
     * 登录
     */
    public void loginUser(LoginInfo loginInfo) {
        Call<UserLoginResponseBean> callLogin = RetrofitManager.getInstance().getService().login(loginInfo);
        callLogin.enqueue(new Callback<UserLoginResponseBean>() {
            @Override
            public void onResponse(Call<UserLoginResponseBean> call, Response<UserLoginResponseBean> response) {
                Log.i(TAG, "onResponse: " + response.message());
                if (response.body().getCode() == CodeUtil.Succeed) {
                    if (onUserInfoGetListener != null)
                        onUserInfoGetListener.onSucceed(response.body().getData());
                } else {
                    if (onUserInfoGetListener != null)
                        onUserInfoGetListener.onFailed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponseBean> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                if (onUserInfoGetListener != null) {
                    onUserInfoGetListener.onFailed(mAppContext.getString(R.string.login_error));
                }
            }
        });

    }

    /**
     * 获取用户的信息
     *
     * @param userId
     */
    public void getUserInfo(int userId) {

        Call<UserLoginResponseBean> callUserInfo = RetrofitManager.getInstance().getService().getUserInfo(new UserIdBean(userId));
        callUserInfo.enqueue(new Callback<UserLoginResponseBean>() {
            @Override
            public void onResponse(Call<UserLoginResponseBean> call, Response<UserLoginResponseBean> response) {
                Log.i(TAG, "onResponse:" + response.body().getMessage());
                if (response.body().getCode() == CodeUtil.Succeed) {
                    if (onUserInfoGetListener != null)
                        onUserInfoGetListener.onSucceed(response.body().getData());
                } else {
                    if (onUserInfoGetListener != null)
                        onUserInfoGetListener.onFailed(response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<UserLoginResponseBean> call, Throwable t) {
                Log.e(TAG, "onResponse:" + t);
                if (onUserInfoGetListener != null)
                    onUserInfoGetListener.onFailed(mAppContext.getString(R.string.get_userinfo_error));
            }
        });

    }
}
