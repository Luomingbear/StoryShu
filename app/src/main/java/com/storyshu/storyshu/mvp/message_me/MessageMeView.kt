package com.storyshu.storyshu.mvp.message_me

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.storyshu.storyshu.bean.getStory.StoryIdBean
import com.storyshu.storyshu.mvp.view.base.IBaseView

/**
 * Created by bear on 2017/9/16.
 */
interface MessageMeView : IBaseView {
    val messageRv: RecyclerView

    val refreshLayout: SwipeRefreshLayout

    fun getMessageType(): Int

    fun intentStoryRoom(storyIdBean: StoryIdBean)
}