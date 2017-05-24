package com.storyshu.storyshu.mvp.message;

import android.support.v4.widget.SwipeRefreshLayout;

import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.mvp.view.base.IBaseView;
import com.storyshu.storyshu.widget.IExpandableListView;

/**
 * mvp模式
 * 消息页面的view接口
 * Created by bear on 2017/3/21.
 */

public interface MessageView extends IBaseView {

    /**
     * 获取下拉刷新
     * @return
     */
    SwipeRefreshLayout getRefreshLayout();

    /**
     * 获取点赞折叠的列表控件
     *
     * @return
     */
    IExpandableListView getLikeMessageList();

    /**
     * 获取评论折叠的列表控件
     *
     * @return
     */
    IExpandableListView getCommentMessageList();

    /**
     * 获取系统消息折叠的列表控件
     *
     * @return
     */
    IExpandableListView getSystemMessageList();

    /**
     * 跳转到故事屋
     *
     * @param storyIdBean
     */
    void intent2StoryRoom(StoryIdBean storyIdBean);
}
