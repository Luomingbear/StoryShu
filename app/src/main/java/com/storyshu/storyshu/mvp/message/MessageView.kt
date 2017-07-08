package com.storyshu.storyshu.mvp.message

import android.support.v4.widget.SwipeRefreshLayout

import com.storyshu.storyshu.bean.getStory.StoryIdBean
import com.storyshu.storyshu.mvp.view.base.IBaseView
import com.storyshu.storyshu.widget.IExpandableListView

/**
 * mvp模式
 * 消息页面的view接口
 * Created by bear on 2017/3/21.
 */

interface MessageView : IBaseView {

    /**
     * 获取下拉刷新
     * @return
     */
    val refreshLayout: SwipeRefreshLayout

    /**
     * 获取点赞折叠的列表控件

     * @return
     */
    val likeMessageList: IExpandableListView

    /**
     * 获取评论折叠的列表控件

     * @return
     */
    val commentMessageList: IExpandableListView

    /**
     * 获取系统消息折叠的列表控件

     * @return
     */
    val systemMessageList: IExpandableListView

    /**
     * 跳转到故事屋

     * @param storyIdBean
     */
    fun intent2StoryRoom(storyIdBean: StoryIdBean)
}
