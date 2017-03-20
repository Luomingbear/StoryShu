package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 位置数据
 * Created by bear on 2017/3/17.
 */

public class LocationInfo implements Parcelable {
    private int locationId; //
    private String describe; //描述

    private float Lat; //经度

    private float Lng; //纬度

    public LocationInfo(int locationId, String describe, float lat, float lng) {
        this.locationId = locationId;
        this.describe = describe;
        Lat = lat;
        Lng = lng;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.locationId);
        dest.writeString(this.describe);
        dest.writeFloat(this.Lat);
        dest.writeFloat(this.Lng);
    }

    protected LocationInfo(Parcel in) {
        this.locationId = in.readInt();
        this.describe = in.readString();
        this.Lat = in.readFloat();
        this.Lng = in.readFloat();
    }

    public static final Parcelable.Creator<LocationInfo> CREATOR = new Parcelable.Creator<LocationInfo>() {
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
