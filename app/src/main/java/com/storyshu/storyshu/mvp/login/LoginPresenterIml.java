package com.storyshu.storyshu.mvp.login;

import android.content.Context;
import android.text.TextUtils;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.EmailFormatCheckUtil;

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
        if (checkInput()) {
            mMvpView.intent2MainActivity();
        }
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
            //todo 验证密码
            if (mMvpView.getPassword().equals("0000"))
                return true;

            mMvpView.showToast(R.string.login_password_illegal);
            return false;
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
