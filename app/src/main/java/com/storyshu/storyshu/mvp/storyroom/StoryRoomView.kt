package com.storyshu.storyshu.mvp.storyroom

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.TextView

import com.storyshu.storyshu.bean.getStory.StoryBean
import com.storyshu.storyshu.mvp.view.base.IBaseActivityView
import com.storyshu.storyshu.widget.ClickButton
import com.storyshu.storyshu.widget.text.RoundTextView

/**
 * mvp模式
 * 故事屋的视图
 * Created by bear on 2017/3/30.
 */

interface StoryRoomView : IBaseActivityView {

    /**
     * 获取下拉刷新控件

     * @return
     */
    val refreshLayout: SwipeRefreshLayout

    /**
     * 获取故事的点赞按钮

     * @return
     */
    val likeButton: ClickButton

    /**
     * 获取故事的喝倒彩按钮

     * @return
     */
    val opposeButton: ClickButton

    /**
     * 获取故事的评论按钮

     * @return
     */
    val commentButton: ClickButton

    /**
     * 获取评论的输入框

     * @return
     */
    val commentEdit: EditText

    /**
     * 获取发送按钮

     * @return
     */
    val sendButton: RoundTextView

    /**
     * 获取显示评论的RecyclerView

     * @return
     */
    val commentRV: RecyclerView

    /**
     * 获取热门评论提示

     * @return
     */
    val hotCommentHit: TextView

    /**
     * 获取故事的配图数据

     * @return
     */
    /**
     * 设置配图数据

     * @param storyPics
     */
    var storyPic: List<String>

    /**
     * 获取故事id
     */
    val storyId: String

    /**
     * 设置故事数据

     * @param storyData
     */
    fun setStoryData(storyData: StoryBean)

    /**
     * 跳转到讨论页面
     */
    fun intent2Discuss()

    /**
     * 跳转到用户简介页面
     */
    fun intent2UserInfo()
}
