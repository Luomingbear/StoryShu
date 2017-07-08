package com.storyshu.storyshu.mvp.message

/**
 * mvp模式
 * 消息页面的代理人接口
 * Created by bear on 2017/3/21.
 */

interface MessagePresenter {
    /**
     * 列表的类型
     */
    enum class ListType {
        LIKE,

        COMMENT,

        COMPUTER
    }

    /**
     * 获取信息列表数据；
     */
    fun getMessageData()

    /**
     * 显示信息列表
     */
    fun showMessageList()

    /**
     * 展开列表
     */
    fun unFoldList(listType: ListType)

    /**
     * 折叠列表
     */
    fun FoldList(listType: ListType)

    /**
     * 跳转到故事屋
     */
    fun toStoryRoom()

    /**
     * 跳转系统信息详情
     */
    fun toComputerMessage()
}
