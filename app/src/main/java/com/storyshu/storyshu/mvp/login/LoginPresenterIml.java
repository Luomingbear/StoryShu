package com.storyshu.storyshu.mvp.login;

import android.content.Context;
import android.text.TextUtils;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.database.DateBaseHelperIml;
import com.storyshu.storyshu.info.BaseUserInfo;
import com.storyshu.storyshu.info.LoginInfo;
import com.storyshu.storyshu.model.UserModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.EmailFormatCheckUtil;
import com.storyshu.storyshu.utils.PasswordUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

/**
 * mvp模式
 * 登录页面的功能实现
 * Created by bear on 2017/3/22.
 */

public class LoginPresenterIml extends IBasePresenter<LoginView> implements LoginPresenter {
    public LoginPresenterIml(Context mContext, LoginView mvpView) {
        super(mContext, mvpView);
    }

    @Override
    public boolean VerifyUsername() {
        return false;
    }

    /**
     * 登录
     */
    private void login() {
        //如果输入的值正确则登录
        checkInput();
    }

    /**
     * 检测输入的值
     *
     * @return 已经输入的值是否正确
     */
    private boolean checkInput() {
        //邮箱
        if (TextUtils.isEmpty(mMvpView.getUsername())) {
            mMvpView.showToast(R.string.login_username_empty);
            return false;
        } else if (!EmailFormatCheckUtil.isEmail(mMvpView.getUsername())) {
            mMvpView.showToast(R.string.login_username_illegal);
            return false;
        }
        //密码
        if (TextUtils.isEmpty(mMvpView.getPassword()))
            mMvpView.showToast(R.string.login_password_empty);
        else {

            UserModel userModel = new UserModel(mContext);
            userModel.loginUser(new LoginInfo(mMvpView.getUsername(),
                    PasswordUtil.getEncodeUsernamePassword(mMvpView.getUsername(), mMvpView.getPassword())));
            userModel.setOnUserInfoGetListener(new UserModel.OnUserInfoGetListener() {
                @Override
                public void onSucceed(BaseUserInfo userInfo) {
                    ToastUtil.Show(mContext, R.string.login_succeed);
                    DateBaseHelperIml dateBaseHelperIml = new DateBaseHelperIml(mContext);
                    dateBaseHelperIml.insertUserData(userInfo);

                    ISharePreference.saveUserId(mContext, userInfo.getUserId());
                    mMvpView.intent2MainActivity();
                }

                @Override
                public void onFailed(String error) {
                    ToastUtil.Show(mContext, error);
                }
            });
        }
        return true;
    }

    @Override
    public void loginUser() {
        login();
    }

    @Override
    public void goRegister() {
        mMvpView.intent2RegisterActivity();
    }

    @Override
    public void forgotPassword() {
        mMvpView.showToast(R.string.forgot_password);
    }
}
