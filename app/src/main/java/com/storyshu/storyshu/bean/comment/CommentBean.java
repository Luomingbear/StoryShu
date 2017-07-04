package com.storyshu.storyshu.bean.comment;

import com.storyshu.storyshu.info.BaseUserInfo;

/**
 * 单个评论的数据
 * Created by bear on 2017/5/15.
 */

public class CommentBean {
    private BaseUserInfo userInfo;
    private String commentId;
    private String storyId;
    private String comment;
    private String createTime;
    private int likeNum;
    private int opposeNum;
    private String tag;
    private int commentType = COMMENT; //评论的类型，是评论文章还是回复的评论

    private String reply; //回复的内容
    private String replyUser; //被回复的用户的昵称

    public static final int COMMENT = 0; //评论
    public static final int REPLY = 1; //回复

    public CommentBean() {
    }

    public CommentBean(BaseUserInfo userInfo, String commentId, String storyId, String comment,
                       String createTime, int likeNum, int opposeNum, String tag, int commentType,
                       String reply, String replyUser) {
        this.userInfo = userInfo;
        this.commentId = commentId;
        this.storyId = storyId;
        this.comment = comment;
        this.createTime = createTime;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
        this.tag = tag;
        this.commentType = commentType;
        this.reply = reply;
        this.replyUser = replyUser;
    }

    public BaseUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BaseUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getOpposeNum() {
        return opposeNum;
    }

    public void setOpposeNum(int opposeNum) {
        this.opposeNum = opposeNum;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(String replyUser) {
        this.replyUser = replyUser;
    }
}
