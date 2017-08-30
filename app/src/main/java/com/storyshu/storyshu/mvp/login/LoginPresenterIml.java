package com.storyshu.storyshu.mvp.login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.BaseUserInfo;
import com.storyshu.storyshu.info.LoginInfo;
import com.storyshu.storyshu.model.UserModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.EmailFormatCheckUtil;
import com.storyshu.storyshu.utils.PasswordUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.dialog.LoadingDialog;

/**
 * mvp模式
 * 登录页面的功能实现
 * Created by bear on 2017/3/22.
 */

public class LoginPresenterIml extends IBasePresenter<LoginView> implements LoginPresenter {
    private LoadingDialog dialog;

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
            dialog = new LoadingDialog(mContext, R.style.TransparentDialogTheme);
            dialog.show();

            //
            UserModel userModel = new UserModel(mContext);
            userModel.loginUser(new LoginInfo(mMvpView.getUsername(),
                    PasswordUtil.getEncodeUsernamePassword(mMvpView.getUsername(), mMvpView.getPassword())));
            userModel.setOnUserInfoGetListener(new UserModel.OnUserInfoGetListener() {
                @Override
                public void onSucceed(BaseUserInfo userInfo) {

                    //登陆环信服务器
                    loginHX(String.valueOf(userInfo.getUserId()));
                }

                @Override
                public void onFailed(String error) {
                    ToastUtil.Show(mContext, error);
                    dialog.dismiss();
                }
            });

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
        if (TextUtils.isEmpty(mMvpView.getPassword())) {
            mMvpView.showToast(R.string.login_password_empty);
            return false;
        }

        return true;
    }

    /**
     * 登陆环信账号
     */
    private void loginHX(final String userId) {
        EMClient.getInstance().login(userId,
                PasswordUtil.getEncodeUsernamePassword(mMvpView.getUsername(), mMvpView.getPassword()),
                new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("main", "登录聊天服务器成功！");

                        ISharePreference.saveUserId(mContext, Integer.parseInt(userId));

                        dialog.dismiss();
                        mMvpView.intent2MainActivity();
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        dialog.dismiss();

                        Log.d("main", "登录聊天服务器失败!");
                    }
                });
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
