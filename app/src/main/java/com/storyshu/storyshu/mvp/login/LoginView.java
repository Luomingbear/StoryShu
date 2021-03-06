package com.storyshu.storyshu.mvp.login;

import com.storyshu.storyshu.mvp.view.base.IBaseActivityView;

/**
 * mvp模式
 * 登录界面
 * Created by bear on 2017/3/22.
 */

public interface LoginView extends IBaseActivityView {
    /**
     * 获取账号
     *
     * @return
     */
    String getUsername();

    /**
     * 获取密码
     *
     * @return
     */
    String getPassword();


    /**
     * 显示登录中
     */
    void showLoading();

    /**
     * 到主界面
     */
    void intent2MainActivity();

    /**
     * 跳转注册页面
     */
    void intent2RegisterActivity();
}
