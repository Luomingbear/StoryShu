package com.storyshu.storyshu.mvp.register;

import android.content.Context;
import android.text.TextUtils;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.data.DateBaseHelperIml;
import com.storyshu.storyshu.info.BaseUserInfo;
import com.storyshu.storyshu.info.RegisterUserInfo;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.EmailFormatCheckUtil;
import com.storyshu.storyshu.utils.PasswordUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

/**
 * mvp模式
 * 注册账号页面的控制实现
 * Created by bear on 2017/3/22.
 */

public class RegisterPresenterIml extends IBasePresenter<RegisterView>implements RegisterPresenter {
    private static final String TAG = "RegisterPresent  erIml";
    private int mStep = 1; //当前在第几步
    private int minPasswordLength = 8; //密码的最少位数

    public RegisterPresenterIml(Context mContext, RegisterView mvpView) {
        super(mContext, mvpView);
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
        if (TextUtils.isEmpty(mMvpView.getUsername()))
            mMvpView.showToast(R.string.login_username_empty);
        else {
            if (EmailFormatCheckUtil.isEmail(mMvpView.getUsername())) {
                // TODO: 2017/3/24 检查用户名是否已经注册
                if (TextUtils.isEmpty(mMvpView.getPassword()))
                    mMvpView.showToast(R.string.login_password_empty);
                else if (mMvpView.getPassword().length() < minPasswordLength) {
                    mMvpView.showToast(mContext.getString(R.string.login_password_too_sort,
                            minPasswordLength));
                } else {
                    mMvpView.change2StepTwo();
                    mStep = 2;
                }
            } else {
                mMvpView.showToast(R.string.login_username_illegal);
            }
        }
    }

    /**
     * 检查用户信息的完整性
     */
    private void checkUserInfo() {
        if (TextUtils.isEmpty(mMvpView.getNickname()))
            mMvpView.showToast(R.string.nickname_empty);
        else {
            if (TextUtils.isEmpty(mMvpView.getAvatar()))
                mMvpView.showToast(R.string.avatar_empty);
            else {
                registerUser();
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

    /**
     * 注册
     */
    @Override
    public void registerUser() {
        // TODO: 2017/3/29 注册账号
        RegisterUserInfo userInfo = new RegisterUserInfo();
        userInfo.setEmail(mMvpView.getUsername());        //暂时是邮箱
        userInfo.setPassword(PasswordUtil.getEncodeUsernamePassword(mMvpView.getUsername(), mMvpView.getPassword()));
        userInfo.setNickname(mMvpView.getNickname());
        userInfo.setAvatar(mMvpView.getAvatar());
        DateBaseHelperIml dateBaseHelperIml = new DateBaseHelperIml(mContext);
        dateBaseHelperIml.insertUserData(userInfo);

        //存储小的本地数据
        BaseUserInfo baseuserInfo = new BaseUserInfo();
        baseuserInfo.setUserId(mMvpView.getUsername().hashCode());
        baseuserInfo.setNickname(mMvpView.getNickname());
        baseuserInfo.setAvatar(mMvpView.getAvatar());
        ISharePreference.saveUserData(mContext, baseuserInfo);
        mMvpView.toMainActivity();
    }
}
