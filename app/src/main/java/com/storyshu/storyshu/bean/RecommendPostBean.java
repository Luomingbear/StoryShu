package com.storyshu.storyshu.bean;

/**
 * 获取推荐的故事时的发送值
 * Created by bear on 2017/5/20.
 */

public class RecommendPostBean {
    private int userId;
    private String cityName;

    public RecommendPostBean() {
    }

    public RecommendPostBean(int userId, String cityName) {
        this.userId = userId;
        this.cityName = cityName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
