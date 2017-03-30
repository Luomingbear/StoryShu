package com.storyshu.storyshu.mvp.storyroom;

import android.support.v7.widget.RecyclerView;

import com.storyshu.storyshu.mvp.view.base.IBaseView;
import com.storyshu.storyshu.widget.ClickButton;

/**
 * mvp模式
 * 故事屋的视图
 * Created by bear on 2017/3/30.
 */

public interface StoryRoomView extends IBaseView {
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
}
