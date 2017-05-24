package com.storyshu.storyshu.bean.message;

import com.storyshu.storyshu.info.BaseUserInfo;

/**
 * 故事获得的赞
 * Created by bear on 2017/5/24.
 */

public class StoryLikeBean {
    private BaseUserInfo userInfo;
    private String storyId;
    private String content;
    private String cover;
    private String createTime;

    public StoryLikeBean() {
    }

    public StoryLikeBean(BaseUserInfo userInfo, String storyId, String content, String cover, String createTime) {
        this.userInfo = userInfo;
        this.storyId = storyId;
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
