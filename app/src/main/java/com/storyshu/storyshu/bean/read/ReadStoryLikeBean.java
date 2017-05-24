package com.storyshu.storyshu.bean.read;

/**
 * 标记已读点赞信息
 * Created by bear on 2017/5/25.
 */

public class ReadStoryLikeBean {
    private int userId;
    private String storyId;

    public ReadStoryLikeBean() {
    }

    public ReadStoryLikeBean(int userId, String storyId) {
        this.userId = userId;
        this.storyId = storyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }
}
