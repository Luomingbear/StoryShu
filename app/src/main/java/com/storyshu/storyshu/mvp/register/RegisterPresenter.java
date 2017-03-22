package com.storyshu.storyshu.mvp.register;

/**
 * mvp模式
 * 注册账号的页面的控制
 * Created by bear on 2017/3/22.
 */

public interface RegisterPresenter {
    /**
     * 下一步
     */
    void nextStep();

    /**
     * 注册账号
     */
    void registerUser();
}
