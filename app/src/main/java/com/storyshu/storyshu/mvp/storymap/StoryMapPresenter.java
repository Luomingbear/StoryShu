package com.storyshu.storyshu.mvp.storymap;

/**
 * mvp模式
 * 故事地图的控制
 * Created by bear on 2017/3/26.
 */

public interface StoryMapPresenter {
    /**
     * 初始化地图
     */
    void initMap();

    /**
     * 显示用户集图标
     * 不包括用户自己位置的图标
     */
    void showStoryIcons();

    /**
     * 显示签到弹窗
     */
    void showSignDialog();
}
