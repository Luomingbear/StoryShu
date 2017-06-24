package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

/**
 * 故事卡片信息类
 * Created by bear on 2016/12/4.
 */

public class CardInfo implements Parcelable {
    private BaseUserInfo userInfo;
    private String storyId;
    private String content;
    private String cover = null;
    private String locationTitle;
    private double latitude;
    private double longitude;
    private String createTime;
    private String destroyTime;
    private Boolean anonymous;
    private Boolean like = false; //点赞
    private Boolean oppose = false; //反对
    private String title; //文章的标题
    private int storyType = STORY; //故事的类型：短文；文章；视频

    private int likeNum;
    private int opposeNum;
    private int commentNum;


    //文章的类型
    public static final int STORY = 0; //类似微博，140字以内
    public static final int ARTICLE = 1; //类似简书，不限字数，有标题
    public static final int VIDEO = 2; //视频

    public CardInfo() {

    }

    public CardInfo(BaseUserInfo userInfo, String storyId, String content, String cover, String locationTitle,
                    double latitude, double longitude, String createTime, String destroyTime, Boolean anonymous,
                    Boolean like, Boolean oppose, String title, int storyType, int likeNum, int opposeNum, int commentNum) {
        this.userInfo = userInfo;
        this.storyId = storyId;
        this.content = content;
        this.cover = cover;
        this.locationTitle = locationTitle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createTime = createTime;
        this.destroyTime = destroyTime;
        this.anonymous = anonymous;
        this.like = like;
        this.oppose = oppose;
        this.title = title;
        this.storyType = storyType;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
        this.commentNum = commentNum;
    }

    public BaseUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BaseUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public LatLng getLatLng() {
        return new LatLng(this.latitude, this.longitude);
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDestroyTime() {
        return destroyTime;
    }

    public void setDestroyTime(String destroyTime) {
        this.destroyTime = destroyTime;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean getLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean getOppose() {
        return oppose;
    }

    public void setOppose(boolean oppose) {
        this.oppose = oppose;
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

    public void setLike(Boolean like) {
        this.like = like;
    }

    public void setOppose(Boolean oppose) {
        this.oppose = oppose;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStoryType() {
        return storyType;
    }

    public void setStoryType(int storyType) {
        this.storyType = storyType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userInfo, flags);
        dest.writeString(this.storyId);
        dest.writeString(this.content);
        dest.writeString(this.cover);
        dest.writeString(this.locationTitle);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.createTime);
        dest.writeString(this.destroyTime);
        dest.writeValue(this.anonymous);
        dest.writeValue(this.like);
        dest.writeValue(this.oppose);
        dest.writeString(this.title);
        dest.writeInt(this.storyType);
        dest.writeInt(this.likeNum);
        dest.writeInt(this.opposeNum);
        dest.writeInt(this.commentNum);
    }

    protected CardInfo(Parcel in) {
        this.userInfo = in.readParcelable(BaseUserInfo.class.getClassLoader());
        this.storyId = in.readString();
        this.content = in.readString();
        this.cover = in.readString();
        this.locationTitle = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.createTime = in.readString();
        this.destroyTime = in.readString();
        this.anonymous = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.like = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.oppose = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.title = in.readString();
        this.storyType = in.readInt();
        this.likeNum = in.readInt();
        this.opposeNum = in.readInt();
        this.commentNum = in.readInt();
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
