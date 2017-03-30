package com.storyshu.storyshu.info;

import android.os.Parcel;

import com.amap.api.maps.model.LatLng;

/**
 * 故事信息
 * Created by bear on 2016/12/27.
 */

public class StoryInfo extends CardInfo {
    private String Content; //正文
    private LatLng latLng; //坐标

    public StoryInfo() {
    }

    public StoryInfo(String storyId, String cover, String content, BaseUserInfo userInfo, String createDate,
                     int lifeTime, String location, int likeNum, int opposeNum, boolean isAnonymous, String content1, LatLng latLng) {
        super(storyId, cover, content, userInfo, createDate, lifeTime, location, likeNum, opposeNum, isAnonymous);
        Content = content1;
        this.latLng = latLng;
    }

    public StoryInfo(Parcel in, String content, LatLng latLng) {
        super(in);
        Content = content;
        this.latLng = latLng;
    }

    @Override
    public String getContent() {
        return Content;
    }

    @Override
    public void setContent(String content) {
        Content = content;
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
        super.writeToParcel(dest, flags);
        dest.writeString(this.Content);
        dest.writeParcelable(this.latLng, flags);
    }

    protected StoryInfo(Parcel in) {
        super(in);
        this.Content = in.readString();
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<StoryInfo> CREATOR = new Creator<StoryInfo>() {
        @Override
        public StoryInfo createFromParcel(Parcel source) {
            return new StoryInfo(source);
        }

        @Override
        public StoryInfo[] newArray(int size) {
            return new StoryInfo[size];
        }
    };
}
