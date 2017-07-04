package com.storyshu.storyshu.bean.comment;

/**
 * 回复的数据结构
 * Created by bear on 2017/6/29.
 */

public class ReplyPostBean extends CommentPostBean{
    private String replyId; //回复的评论对象的commentId


    public ReplyPostBean() {
    }

    public ReplyPostBean(String storyId, int userId, String comment, String createTime, String replyId) {
        super(storyId, userId, comment, createTime);
        this.replyId = replyId;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }
}
