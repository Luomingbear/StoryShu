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
    private String content; //故事的内容
    private UserInfo userInfo; //用户信息
    private String createDate; //发布时间
    private int lifeTime = 24; //保存时间，单位小时
    private String location; //地点

    private int likeNum = 0; //点赞数量
    private int opposeNum = 0; //反对数量

    public CardInfo() {
    }

    public CardInfo(StoryInfo storyInfo) {
        this.storyId = storyInfo.getStoryId();
        this.detailPic = storyInfo.getDetailPic();
        this.content = this.getContent();
        this.userInfo = storyInfo.getUserInfo();
        this.createDate = storyInfo.getCreateDate();
        this.location = storyInfo.getLocation();
    }

    public CardInfo(int storyId, String detailPic, String extract, UserInfo userInfo,
                    String createDate, int lifeTime, String location, int likeNum, int opposeNum) {
        this.storyId = storyId;
        this.detailPic = detailPic;
        this.content = extract;
        this.userInfo = userInfo;
        this.createDate = createDate;
        this.lifeTime = lifeTime;
        this.location = location;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
    }

    public CardInfo(int storyId, String detailPic, String extract, String nickname,
                    int userId, String headPortrait, String createDate, int lifeTime, String location, int likeNum, int opposeNum) {
        this.storyId = storyId;
        this.detailPic = detailPic;
        this.content = extract;
        this.userInfo = new UserInfo(nickname, userId, headPortrait);
        this.createDate = createDate;
        this.lifeTime = lifeTime;
        this.location = location;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getOpposeNum() {
        return opposeNum;
    }

    public void setOpposeNum(int opposeNum) {
        this.opposeNum = opposeNum;
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
        dest.writeString(this.content);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeString(this.createDate);
        dest.writeInt(this.lifeTime);
        dest.writeString(this.location);
        dest.writeInt(this.likeNum);
        dest.writeInt(this.opposeNum);
    }

    protected CardInfo(Parcel in) {
        this.storyId = in.readInt();
        this.detailPic = in.readString();
        this.content = in.readString();
        this.userInfo = in.readParcelable(UserInfo.class.getClassLoader());
        this.createDate = in.readString();
        this.lifeTime = in.readInt();
        this.location = in.readString();
        this.likeNum = in.readInt();
        this.opposeNum = in.readInt();
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
