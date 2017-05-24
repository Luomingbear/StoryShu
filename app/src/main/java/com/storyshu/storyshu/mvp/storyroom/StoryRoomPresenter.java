package com.storyshu.storyshu.mvp.storyroom;

/**
 * mvp模式
 * 故事屋的控制接口
 * Created by bear on 2017/3/30.
 */

public interface StoryRoomPresenter {
    /**
     * 获取评论的数据
     */
    void updateComments();

    /**
     * 显示故事配图预览的弹窗
     */
    void showStoryPicDialog();

    /**
     * 点赞
     */
    void clickLike();

    /**
     * 点击喝倒彩
     */
    void clickOppose();

    /**
     * 点击添加评论
     */
    void clickSend();

    /**
     * 获取故事详情
     */
    void getStoryInfo();

    /**
     * 获取故事配图
     */
    void getStoryPic();
}
