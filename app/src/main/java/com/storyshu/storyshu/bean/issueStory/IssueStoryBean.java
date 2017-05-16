package com.storyshu.storyshu.bean.issueStory;

import java.util.List;

/**
 * 创建故事时需要上传的数据
 * Created by bear on 2017/5/11.
 */

public class IssueStoryBean {
    private int userId; //用户id
    private String content; //故事的内容
    private String cover; //故事的封面图
    private List<String> storyPictures; //配图
    private String cityName; //城市名
    private String locationTitle; //地点描述
    private double latitude;
    private double longitude;
    private String createTime; //发布时间
    private String destroyTime; //保存时间，单位分钟
    private Boolean isAnonymous = false;

    public IssueStoryBean() {
    }

    public IssueStoryBean(int userId, String content, String cover, List<String> storyPictures, String cityName, String locationTitle,
                          double latitude, double longitude, String createTime, String destroyTime, Boolean isAnonymous) {
        this.userId = userId;
        this.content = content;
        this.cover = cover;
        this.storyPictures = storyPictures;
        this.cityName = cityName;
        this.locationTitle = locationTitle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createTime = createTime;
        this.destroyTime = destroyTime;
        this.isAnonymous = isAnonymous;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        if (storyPictures != null && storyPictures.size() > 0)
            return storyPictures.get(0);

        return null;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getStoryPictures() {
        return storyPictures;
    }

    public void setStoryPictures(List<String> storyPictures) {
        this.storyPictures = storyPictures;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDestroyTime() {
        return destroyTime;
    }

    public void setDestroyTime(String destroyTime) {
        this.destroyTime = destroyTime;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean anonymous) {
        isAnonymous = anonymous;
    }
}
