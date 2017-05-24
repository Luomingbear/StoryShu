package com.storyshu.storyshu.bean.message;

import com.storyshu.storyshu.info.BaseUserInfo;

/**
 * 单个故事评论信息
 * Created by bear on 2017/5/24.
 */

public class StoryCommentBean {
    private BaseUserInfo userInfo;
    private String storyId;
    private String commentId;
    private String comment;
    private String content;
    private String cover;
    private String createTime;

    public StoryCommentBean() {
    }

    public StoryCommentBean(BaseUserInfo userInfo, String storyId, String commentId,
                            String comment, String content, String cover, String createTime) {
        this.userInfo = userInfo;
        this.storyId = storyId;
        this.commentId = commentId;
        this.comment = comment;
        this.content = content;
        this.cover = cover;
        this.createTime = createTime;
    }

    public BaseUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BaseUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
