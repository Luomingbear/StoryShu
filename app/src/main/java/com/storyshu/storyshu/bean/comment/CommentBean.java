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

    public CommentBean() {
    }

    public CommentBean(BaseUserInfo userInfo, String commentId, String storyId, String comment, String createTime, int likeNum, int opposeNum) {
        this.userInfo = userInfo;
        this.commentId = commentId;
        this.storyId = storyId;
        this.comment = comment;
        this.createTime = createTime;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
    }

    public CommentBean(BaseUserInfo userInfo, String commentId, String storyId, String comment, String createTime, int likeNum, int opposeNum, String tag) {
        this.userInfo = userInfo;
        this.commentId = commentId;
        this.storyId = storyId;
        this.comment = comment;
        this.createTime = createTime;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
        this.tag = tag;
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
}
