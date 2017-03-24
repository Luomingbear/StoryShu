package com.storyshu.storyshu.mvp.login;

import android.content.Context;
import android.text.TextUtils;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.EmailFormatCheckUtil;

/**
 * mvp模式
 * 登录页面的功能实现
 * Created by bear on 2017/3/22.
 */

public class LoginPresenterIml implements LoginPresenter {
    private LoginView mLoginView;
    private Context context;

    public LoginPresenterIml(LoginView mLoginView, Context context) {
        this.mLoginView = mLoginView;
        this.context = context;
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
            mLoginView.intent2MainActivity();
        }
    }

    /**
     * 检测输入的值
     *
     * @return 已经输入的值是否正确
     */
    private boolean checkInput() {
        //邮箱
        if (TextUtils.isEmpty(mLoginView.getUsername())) {
            mLoginView.showToast(R.string.login_username_empty);
            return false;
        } else if (!EmailFormatCheckUtil.isEmail(mLoginView.getUsername())) {
            mLoginView.showToast(R.string.login_username_illegal);
            return false;
        }
        //密码
        if (TextUtils.isEmpty(mLoginView.getPassword()))
            mLoginView.showToast(R.string.login_password_empty);
        else {
            //todo 验证密码
            if (mLoginView.getPassword().equals("0000"))
                return true;

            mLoginView.showToast(R.string.login_password_illegal);
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
        mLoginView.intent2RegisterActivity();
    }

    @Override
    public void forgotPassword() {
        mLoginView.showToast(R.string.forgot_password);
    }
}
