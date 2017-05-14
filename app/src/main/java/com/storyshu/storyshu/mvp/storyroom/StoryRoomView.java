package com.storyshu.storyshu.mvp.storyroom;

import android.support.v7.widget.RecyclerView;

import com.storyshu.storyshu.bean.getStory.StoryBean;
import com.storyshu.storyshu.mvp.view.base.IBaseActivityView;
import com.storyshu.storyshu.widget.ClickButton;

import java.util.List;

/**
 * mvp模式
 * 故事屋的视图
 * Created by bear on 2017/3/30.
 */

public interface StoryRoomView extends IBaseActivityView {
    /**
     * 获取故事的点赞按钮
     *
     * @return
     */
    ClickButton getLikeButton();

    /**
     * 获取故事的喝倒彩按钮
     *
     * @return
     */
    ClickButton getOpposeButton();

    /**
     * 获取故事的评论按钮
     *
     * @return
     */
    ClickButton getCommentButton();

    /**
     * 获取显示评论的RecyclerView
     *
     * @return
     */
    RecyclerView getCommentRV();

    /**
     * 获取故事的配图数据
     *
     * @return
     */
    List<String> getStoryPic();

    /**
     * 获取故事id
     */
    String getStoryId();

    /**
     * 设置故事数据
     *
     * @param storyData
     */
    void setStoryData(StoryBean storyData);
}
