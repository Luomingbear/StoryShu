package com.storyshu.storyshu.info;

/**
 * 位置数据
 * Created by bear on 2017/3/17.
 */

public class LocationInfo {
    private String describe; //描述

    private float Lat; //经度

    private float Lng; //纬度

    public LocationInfo(String describe, float lat, float lng) {
        this.describe = describe;
        Lat = lat;
        Lng = lng;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public float getLat() {
        return Lat;
    }

    public void setLat(float lat) {
        Lat = lat;
    }

    public float getLng() {
        return Lng;
    }

    public void setLng(float lng) {
        Lng = lng;
    }
}
