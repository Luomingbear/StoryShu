package com.storyshu.storyshu.model;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.storyshu.storyshu.data.DateBaseHelperIml;
import com.storyshu.storyshu.info.BaseUserInfo;
import com.storyshu.storyshu.info.LoginInfo;
import com.storyshu.storyshu.info.RegisterUserInfo;
import com.storyshu.storyshu.utils.net.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

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

        void onFailed();
    }

    public UserModel(Context mAppContext) {
        this.mAppContext = mAppContext;
    }

    /**
     * 注册账号
     *
     * @param userInfo
     */
    public void register(RegisterUserInfo userInfo) {
        Log.i(TAG, "register: UserInfo:" + userInfo);

        String up = JSON.toJSONString(userInfo);
        try {
            JSONObject obj = new JSONObject(up);
            OkGo.post(UrlUtil.BASE_API_URL + "RegisterUser")
                    .upJson(obj)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.i(TAG, "onSuccess: 获取成功！！" + s);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            Log.e(TAG, "onError: 注册失败！", e);
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     */
    public void loginUser(LoginInfo loginInfo) {
        //初始化OkHttpClient
        String up = JSON.toJSONString(loginInfo);
        try {
            JSONObject json = new JSONObject(up);
            OkGo.post(UrlUtil.BASE_API_URL + "LoginUser")
                    .upJson(json)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                Log.i(TAG, "onSuccess: 获取成功！！" + response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
