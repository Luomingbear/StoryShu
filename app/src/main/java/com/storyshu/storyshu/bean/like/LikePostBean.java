package com.storyshu.storyshu.bean.like;

/**
 * 喜欢和不喜欢上传的数据
 * Created by bear on 2017/5/14.
 */

public class LikePostBean {
    private String storyId;
    private int userId;
    private String createTime;
    private Boolean sure;

    public LikePostBean() {
    }

    public LikePostBean(String storyId, int userId, String createTime, Boolean sure) {
        this.storyId = storyId;
        this.userId = userId;
        this.createTime = createTime;
        this.sure = sure;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Boolean getSure() {
        return sure;
    }

    public void setSure(Boolean sure) {
        this.sure = sure;
    }
}
