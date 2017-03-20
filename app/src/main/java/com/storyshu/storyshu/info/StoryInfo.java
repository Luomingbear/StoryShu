package com.storyshu.storyshu.info;

import android.os.Parcel;

import com.amap.api.services.core.LatLonPoint;

/**
 * 故事信息
 * Created by bear on 2016/12/27.
 */

public class StoryInfo extends CardInfo {
    private String Content; //正文
    private LatLonPoint latLng; //坐标

    public StoryInfo() {
    }

    public StoryInfo(String content, LatLonPoint latLng) {
        Content = content;
        this.latLng = latLng;
    }

    public StoryInfo(StoryInfo storyInfo, String content, LatLonPoint latLng) {
        super(storyInfo);
        Content = content;
        this.latLng = latLng;
    }

    public StoryInfo(int storyId, String detailPic, String extract, UserInfo userInfo, String createDate,
                     int lifeTime, String location, int likeNum, int opposeNum, String content, LatLonPoint latLng) {
        super(storyId, detailPic, extract, userInfo, createDate, lifeTime, location, likeNum, opposeNum);
        Content = content;
        this.latLng = latLng;
    }

    public StoryInfo(int storyId, String detailPic, String extract, String nickname, int userId, String headPortrait,
                     String createDate, int lifeTime, String location, int likeNum, int opposeNum, String content, LatLonPoint latLng) {
        super(storyId, detailPic, extract, nickname, userId, headPortrait, createDate, lifeTime, location, likeNum, opposeNum);
        Content = content;
        this.latLng = latLng;
    }

    public StoryInfo(Parcel in, String content, LatLonPoint latLng) {
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

    public LatLonPoint getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLonPoint latLng) {
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
    }

    protected StoryInfo(Parcel in) {
        super(in);
        this.Content = in.readString();
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
