package com.storyshu.storyshu.mvp.message

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.storyshu.storyshu.bean.getStory.StoryIdBean
import com.storyshu.storyshu.mvp.view.base.IBaseView

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

    val messageRecyclerView: RecyclerView

    /**
     * 跳转到点赞列表

     * @param storyIdBean
     */
    fun intent2MessageMe(type: Int)

    /**
     * 跳转到聊天室
     */
    fun intent2DiscussRoom(storyIdBean: StoryIdBean)

    /**
     * 更新数据显示
     */
    fun notifyDataSetChanged()
}
