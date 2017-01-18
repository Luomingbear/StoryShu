package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 故事卡片信息类
 * Created by bear on 2016/12/4.
 */

public class CardInfo implements Parcelable {
    private int storyId; //故事的id
    private String detailPic; //故事的说明图
    private String title; //故事的标题
    private String extract; //故事的摘要
    private UserInfo userInfo; //用户信息
    private String createDate; //发布时间
    private String location;

    public CardInfo() {
    }

    public CardInfo(StoryInfo storyInfo) {
        this.storyId = storyInfo.getStoryId();
        this.detailPic = storyInfo.getDetailPic();
        this.title = storyInfo.getTitle();
        this.extract = storyInfo.getExtract();
        this.userInfo = storyInfo.getUserInfo();
        this.createDate = storyInfo.getCreateDate();
        this.location = storyInfo.getLocation();
    }

    public CardInfo(int storyId, String detailPic, String title, String extract, UserInfo userInfo, String createDate, String location) {
        this.storyId = storyId;
        this.detailPic = detailPic;
        this.title = title;
        this.extract = extract;
        this.userInfo = userInfo;
        this.createDate = createDate;
        this.location = location;
    }

    public CardInfo(int storyId, String detailPic, String title, String extract, String nickname, int userId, String headPortrait,
                    String createDate, String location) {
        this.storyId = storyId;
        this.detailPic = detailPic;
        this.title = title;
        this.extract = extract;
        this.userInfo = new UserInfo(nickname, userId, headPortrait);
        this.createDate = createDate;
        this.location = location;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getDetailPic() {
        return detailPic;
    }

    public void setDetailPic(String detailPic) {
        this.detailPic = detailPic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.storyId);
        dest.writeString(this.detailPic);
        dest.writeString(this.title);
        dest.writeString(this.extract);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeString(this.createDate);
        dest.writeString(this.location);
    }

    protected CardInfo(Parcel in) {
        this.storyId = in.readInt();
        this.detailPic = in.readString();
        this.title = in.readString();
        this.extract = in.readString();
        this.userInfo = in.readParcelable(UserInfo.class.getClassLoader());
        this.createDate = in.readString();
        this.location = in.readString();
    }

    public static final Creator<CardInfo> CREATOR = new Creator<CardInfo>() {
        @Override
        public CardInfo createFromParcel(Parcel source) {
            return new CardInfo(source);
        }

        @Override
        public CardInfo[] newArray(int size) {
            return new CardInfo[size];
        }
    };
}
