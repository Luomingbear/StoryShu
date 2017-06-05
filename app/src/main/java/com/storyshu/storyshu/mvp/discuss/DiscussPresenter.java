package com.storyshu.storyshu.mvp.discuss;

/**
 * mvp
 * 讨论界面的方法接口
 * Created by bear on 2017/5/24.
 */

public interface DiscussPresenter {
    /**
     * 初始化讨论数据
     */
    void initDiscussData();

    /**
     * 发送信息
     */
    void sendMessage();

}
