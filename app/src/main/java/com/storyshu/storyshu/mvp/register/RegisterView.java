package com.storyshu.storyshu.mvp.register;

import android.widget.TextView;

import com.storyshu.storyshu.mvp.view.base.IBaseView;

/**
 * mvp模式
 * 注册的view
 * Created by bear on 2017/3/22.
 */

public interface RegisterView extends IBaseView {
    /**
     * 获取用户名
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
     * 获取下一步的按钮
     *
     * @return
     */
    TextView getNextButton();

    /**
     * 选择头像图片
     */
    void chooseAvatar();

    /**
     * 获取昵称
     *
     * @return
     */
    String getNickname();

    /**
     * 显示登录信息的布局
     */
    void change2StepOne();

    /**
     * 显示账号信息的布局
     */
    void change2StepTwo();
}
