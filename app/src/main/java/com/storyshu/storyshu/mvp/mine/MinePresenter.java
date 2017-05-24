package com.storyshu.storyshu.mvp.mine;

/**
 * mvp
 * 我的页面
 * Created by bear on 2017/4/16.
 */

public interface MinePresenter {
    /**
     * 初始化数据
     */
    void initDate();

    /**
     * 点击了设置
     */
    void clickSetting();

    /**
     * 点击了我的故事
     */
    void clickMyStory();

    /**
     * 点击了我的足迹
     */
    void clickMyFootprint();

    /**
     * 点击了我的排名
     */
    void clickMyRanking();

    /**
     * 点击了我的机票
     */
    void clickMyTicket();

    /**
     * 点击了我的装扮
     */
    void clickMyOrnament();

    /**
     * 退出登录
     */
    void clickQuitApp();

}
