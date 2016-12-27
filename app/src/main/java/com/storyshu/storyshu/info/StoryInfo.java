package com.storyshu.storyshu.info;

import android.os.Parcel;

import java.util.Date;

/**
 * 故事信息
 * Created by bear on 2016/12/27.
 */

public class StoryInfo extends CardInfo {
    private String Content; //正文

    public StoryInfo() {
    }

    public StoryInfo(String content) {
        Content = content;
    }

    public StoryInfo(int storyId, String detailPic, String title, String extract, UserInfo userInfo, Date createDate, String content) {
        super(storyId, detailPic, title, extract, userInfo, createDate);
        Content = content;
    }

    public StoryInfo(int storyId, String detailPic, String title, String extract, String nickname, int userId, String headPortrait, Date createDate, String content) {
        super(storyId, detailPic, title, extract, nickname, userId, headPortrait, createDate);
        Content = content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getContent() {
        return Content;
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
