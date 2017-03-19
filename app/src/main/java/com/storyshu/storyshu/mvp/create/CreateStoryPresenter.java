package com.storyshu.storyshu.mvp.create;

/**
 * MVP模式
 * 写故事讲的代理人
 * Created by bear on 2017/3/17.
 */

public interface CreateStoryPresenter {

    /**
     * 发布故事
     */
    void issueStory();

    /**
     * 获取定位的兴趣的
     */
    void getLocationPoi();

    /**
     * 获取配图
     */
    void getPicList();

    /**
     * 设置故事的保质期
     */
    void setLifeTime();

}
