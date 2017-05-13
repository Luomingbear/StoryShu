package com.storyshu.storyshu.bean;

/**
 * 请求附近的故事的时候上传的数据
 * Created by bear on 2017/5/12.
 */

public class LocationBean {
    private int userId;
    private double latitude;
    private double longitude;
    private int scale = 17;

    public LocationBean() {
    }

    public LocationBean(int userId, double latitude, double longitude, int scale) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.scale = scale;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
