package com.storyshu.storyshu.mvp.register;

import android.content.Context;
import android.text.TextUtils;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.EmailFormatCheckUtil;

/**
 * mvp模式
 * 注册账号页面的控制实现
 * Created by bear on 2017/3/22.
 */

public class RegisterPresenterIml implements RegisterPresenter {
    private Context mContext;
    private RegisterView mRegisterView;
    private int mStep = 1; //当前在第几步
    private int minPasswordLength = 8; //密码的最少位数

    public RegisterPresenterIml(Context mContext, RegisterView mRegisterView) {
        this.mContext = mContext;
        this.mRegisterView = mRegisterView;
    }


    @Override
    public void onBackPressed() {
        if (mStep == 2)
            mStep = 1;
    }

    /**
     * 检查登录信息的完整性和可用性
     */
    private void checkLoginInfo() {
        if (TextUtils.isEmpty(mRegisterView.getUsername()))
            mRegisterView.showToast(R.string.login_username_empty);
        else {
            if (EmailFormatCheckUtil.isEmail(mRegisterView.getUsername())) {
                // TODO: 2017/3/24 检查用户名是否已经注册
                if (TextUtils.isEmpty(mRegisterView.getPassword()))
                    mRegisterView.showToast(R.string.login_password_empty);
                else if (mRegisterView.getPassword().length() < minPasswordLength) {
                    mRegisterView.showToast(mContext.getString(R.string.login_password_too_sort,
                            minPasswordLength));
                } else {
                    mRegisterView.change2StepTwo();
                    mStep = 2;
                }
            } else {
                mRegisterView.showToast(R.string.login_username_illegal);
            }
        }
    }

    /**
     * 检查用户信息的完整性
     */
    private void checkUserInfo() {
        if (TextUtils.isEmpty(mRegisterView.getNickname()))
            mRegisterView.showToast(R.string.nickname_empty);
        else {
            if (TextUtils.isEmpty(mRegisterView.getAvatar()))
                mRegisterView.showToast(R.string.avatar_empty);
            else {
                // TODO: 2017/3/24 注册账号
            }
        }
    }

    @Override
    public void nextStep() {
        if (mStep == 1) {
            checkLoginInfo();
        } else if (mStep == 2) {
            checkUserInfo();
        }
    }

    @Override
    public void registerUser() {

    }
}
