package com.storyshu.storyshu.bean.issueStory;

import com.amap.api.maps.model.LatLng;

/**
 * 发布动态的基本数据信息
 * Created by bear on 2017/6/20.
 */

public class BaseIssueStoryBean {
    private int userId; //用户id
    private String content; //故事的内容
    private String cityName; //城市名
    private String locationTitle; //地点描述
    private double latitude;
    private double longitude;
    private String createTime; //发布时间
    private Boolean isAnonymous = false;

    private int tag = TAG_SORT_STORY;

    public static final int TAG_SORT_STORY = 0;
    public static final int TAG_LONG_STORY = TAG_SORT_STORY + 1;

    public BaseIssueStoryBean() {
    }

    public BaseIssueStoryBean(int userId, String content, String cityName, String locationTitle, double latitude, double longitude, String createTime, Boolean isAnonymous, int tag) {
        this.userId = userId;
        this.content = content;
        this.cityName = cityName;
        this.locationTitle = locationTitle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createTime = createTime;
        this.isAnonymous = isAnonymous;
        this.tag = tag;
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

    public void setLatLng(LatLng latLng) {
        this.setLatitude(latLng.latitude);
        this.setLongitude(latLng.longitude);
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

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean anonymous) {
        isAnonymous = anonymous;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
