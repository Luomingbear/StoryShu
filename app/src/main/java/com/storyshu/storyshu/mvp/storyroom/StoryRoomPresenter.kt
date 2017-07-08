package com.storyshu.storyshu.mvp.storyroom

/**
 * mvp模式
 * 故事屋的控制接口
 * Created by bear on 2017/3/30.
 */

interface StoryRoomPresenter {
    /**
     * 获取评论的数据
     */
    fun updateComments()

    /**
     * 显示故事配图预览的弹窗
     */
    fun showStoryPicDialog()

    /**
     * 点赞
     */
    fun clickLike()

    /**
     * 点击喝倒彩
     */
    fun clickOppose()

    /**
     * 点击添加评论
     */
    fun clickSend()

    /**
     * 获取故事详情
     */
    fun getStoryInfo()

    /**
     * 获取故事配图
     */
    fun getStoryPic()
}
