package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

/**
 * 位置数据
 * Created by bear on 2017/3/17.
 */

public class LocationInfo implements Parcelable {
    private String locationId; //
    private String title; //标题
    private String describe; //描述

    private LatLng latLng; //经纬度

    public LocationInfo() {
    }

    public LocationInfo(String locationId, String title, String describe, LatLng latLng) {
        this.locationId = locationId;
        this.title = title;
        this.describe = describe;
        this.latLng = latLng;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.locationId);
        dest.writeString(this.title);
        dest.writeString(this.describe);
        dest.writeParcelable(this.latLng, flags);
    }

    protected LocationInfo(Parcel in) {
        this.locationId = in.readString();
        this.title = in.readString();
        this.describe = in.readString();
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<LocationInfo> CREATOR = new Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel source) {
            return new LocationInfo(source);
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };
}
