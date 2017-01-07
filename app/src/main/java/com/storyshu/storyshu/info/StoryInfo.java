package com.storyshu.storyshu.info;

import android.os.Parcel;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import java.util.Date;

/**
 * 故事信息
 * Created by bear on 2016/12/27.
 */

public class StoryInfo extends CardInfo {
    private String Content; //正文
    private LatLonPoint latLng;

    public StoryInfo() {
    }

    public StoryInfo(String content) {
        Content = content;
    }

    public StoryInfo(int storyId, String detailPic, String title, String extract, UserInfo userInfo, Date createDate, String content,
                     String location, LatLonPoint latLng) {
        super(storyId, detailPic, title, extract, userInfo, createDate, location);
        Content = content;
        this.latLng = latLng;
    }

    public StoryInfo(int storyId, String detailPic, String title, String extract, String nickname, int userId, String avatar,
                     Date createDate, String content, String location, LatLonPoint latLng) {
        super(storyId, detailPic, title, extract, nickname, userId, avatar, createDate, location);
        Content = content;
        this.latLng = latLng;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getContent() {
        return Content;
    }

    public LatLonPoint getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLonPoint latLng) {
        this.latLng = latLng;
    }

    public void setLatLng(LatLng latLng) {
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        this.latLng = latLonPoint;
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
