package com.storyshu.storyshu.mvp.login;

/**
 * mvp模式
 * 登录页面的控制
 * Created by bear on 2017/3/22.
 */

public interface LoginPresenter {

    /**
     * 检测用户名格式是否正确、是否存在
     *
     * @return
     */
    boolean VerifyUsername();

    /**
     * 登录
     */
    void loginUser();

    /**
     * 前往注册
     */
    void goRegister();

    /**
     * 忘记密码
     */
    void forgotPassword();
}
