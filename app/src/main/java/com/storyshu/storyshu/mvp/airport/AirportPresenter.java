package com.storyshu.storyshu.mvp.airport;

/**
 * 候机厅的逻辑接口
 * Created by bear on 2017/3/20.
 */

public interface AirportPresenter {

    /**
     * 获取推送的故事列表
     */
    void getPushData();

    /**
     * 跳转到故事屋
     */
    void intent2StoryRoom();

    /**
     * 跳转到广告页
     */
    void intent2AdActivity();

    /**
     * 点击点赞
     *
     * @return 点赞是否成功
     */
    boolean clickLike();

    /**
     * 点击喝倒彩
     *
     * @return 喝倒彩是否成功
     */
    boolean clickOppose();
}
