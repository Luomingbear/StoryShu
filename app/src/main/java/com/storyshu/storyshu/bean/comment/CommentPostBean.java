package com.storyshu.storyshu.bean.comment;

/**
 * 上传的评论内容
 * Created by bear on 2017/5/15.
 */

public class CommentPostBean {
    private String storyId;
    private int userId;
    private String comment;
    private String createTime;

    public CommentPostBean() {
    }

    public CommentPostBean(String storyId, int userId, String comment, String createTime) {
        this.storyId = storyId;
        this.userId = userId;
        this.comment = comment;
        this.createTime = createTime;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
}
