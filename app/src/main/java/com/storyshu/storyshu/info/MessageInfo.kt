package com.storyshu.storyshu.info

/**
 * 聊天列表的item的数据结构
 * Created by bear on 2017/9/15.
 */
data class MessageInfo(
        var userId: Int = 0,
        var roomId: String = "",
        var storyId: String = "",
        var cover: String = "",
        var title: String = "",
        var content: String = "",
        var type: Int = DISCUSS
) {
    companion object {
        val DISCUSS = 0 //聊天室
        val CHAT = DISCUSS + 1 //单独聊天
        val COMMENT = CHAT + 1 //评论
        val LIKE = COMMENT + 1//赞
    }
}