package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

import com.storyshu.storyshu.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 故事卡片信息类
 * Created by bear on 2016/12/4.
 */

public class CardInfo implements Parcelable {
    private String storyId; //故事的id
    private String cover; //故事的封面图
    private List<String> storyPic; //配图
    private String content; //故事的内容
    private BaseUserInfo userInfo; //用户信息
    private String createDate; //发布时间
    private int lifeTime = 24 * 60; //保存时间，单位分钟
    private String location; //地点

    private int likeNum = 0; //点赞数量
    private int opposeNum = 0; //反对数量
    private boolean isAnonymous = false; //是否匿名

    public CardInfo() {
    }

    public CardInfo(StoryInfo storyInfo) {
        this.storyId = storyInfo.getStoryId();
        this.cover = storyInfo.getCover();
        this.content = this.getContent();
        this.userInfo = storyInfo.getUserInfo();
        this.createDate = storyInfo.getCreateDate();
        this.location = storyInfo.getLocation();
    }

    public CardInfo(String storyId, String cover, String content, BaseUserInfo userInfo,
                    String createDate, int lifeTime, String location, int likeNum, int opposeNum, boolean isAnonymous) {
        this.storyId = storyId;
        this.cover = cover;
        this.content = content;
        this.userInfo = userInfo;
        this.createDate = createDate;
        this.lifeTime = lifeTime;
        this.location = location;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
        this.isAnonymous = isAnonymous;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getCover() {
        return storyPic == null ? null : storyPic.get(0);
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getStoryPic() {
        return storyPic;
    }

    public void setStoryPic(ArrayList<String> storyPic) {
        this.storyPic = storyPic;
    }

    public void setStoryPic(String storyPics) {
        this.storyPic = ListUtil.StringToStringList(storyPics);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BaseUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BaseUserInfo userInfo) {
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

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.storyId);
        dest.writeString(this.cover);
        dest.writeStringList(this.storyPic);
        dest.writeString(this.content);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeString(this.createDate);
        dest.writeInt(this.lifeTime);
        dest.writeString(this.location);
        dest.writeInt(this.likeNum);
        dest.writeInt(this.opposeNum);
        dest.writeByte(this.isAnonymous ? (byte) 1 : (byte) 0);
    }

    protected CardInfo(Parcel in) {
        this.storyId = in.readString();
        this.cover = in.readString();
        this.storyPic = in.createStringArrayList();
        this.content = in.readString();
        this.userInfo = in.readParcelable(BaseUserInfo.class.getClassLoader());
        this.createDate = in.readString();
        this.lifeTime = in.readInt();
        this.location = in.readString();
        this.likeNum = in.readInt();
        this.opposeNum = in.readInt();
        this.isAnonymous = in.readByte() != 0;
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
