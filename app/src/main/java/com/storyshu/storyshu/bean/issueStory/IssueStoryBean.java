package com.storyshu.storyshu.bean.issueStory;

import java.util.List;

/**
 * 创建故事时需要上传的数据
 * Created by bear on 2017/5/11.
 */

public class IssueStoryBean extends BaseIssueStoryBean {
    private String cover; //故事的封面图
    private List<String> storyPictures; //配图
    private String destroyTime; //保存时间，单位分钟

    public IssueStoryBean() {
    }

    public IssueStoryBean(String cover, List<String> storyPictures, String destroyTime) {
        this.cover = cover;
        this.storyPictures = storyPictures;
        this.destroyTime = destroyTime;
    }

    public IssueStoryBean(int userId, String content, String cityName, String locationTitle, double latitude, double longitude, String createTime, Boolean isAnonymous, int tag, String cover, List<String> storyPictures, String destroyTime) {
        super(userId, content, cityName, locationTitle, latitude, longitude, createTime, isAnonymous, tag);
        this.cover = cover;
        this.storyPictures = storyPictures;
        this.destroyTime = destroyTime;
    }

    public List<String> getStoryPictures() {
        return storyPictures;
    }

    public void setStoryPictures(List<String> storyPictures) {
        this.storyPictures = storyPictures;
    }

    public String getDestroyTime() {
        return destroyTime;
    }

    public void setDestroyTime(String destroyTime) {
        this.destroyTime = destroyTime;
    }

    public String getCover() {
        if (storyPictures != null && storyPictures.size() > 0)
            return storyPictures.get(0);

        return null;
    }

    public void setCover(String cover) {
        if (storyPictures != null && storyPictures.size() > 0)
            this.cover = storyPictures.get(0);
    }

}
