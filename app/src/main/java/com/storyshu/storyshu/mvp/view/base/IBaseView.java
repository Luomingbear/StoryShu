package com.storyshu.storyshu.mvp.view.base;

/**
 * 基本的View接口
 * Created by bear on 2017/3/17.
 */

public interface IBaseView {
    /**
     * 初始化视图
     */
    void initView();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化事件
     */
    void initEvents();

    /**
     * 弹窗显示
     *
     * @param s
     */
    void showToast(String s);

    /**
     * 弹窗显示
     *
     * @param stringRes
     */
    void showToast(int stringRes);
}
