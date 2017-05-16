package com.storyshu.storyshu.bean.getStory;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;
import com.storyshu.storyshu.info.BaseUserInfo;

import java.util.List;

/**
 * 一个故事包含的完整信息
 * Created by bear on 2017/5/12.
 */

public class StoryBean implements Parcelable {
    private BaseUserInfo userInfo;
    private String storyId;
    private String content;
    private String cover;
    private List<String> storyPictures;
    private String locationTitle;
    private double latitude;
    private double longitude;
    private String createTime;
    private String destroyTime;
    private Boolean anonymous;
    private int likeNum;
    private int opposeNum;

    public StoryBean() {
    }

    public StoryBean(BaseUserInfo userInfo, String storyId, String content, String cover,
                     List<String> storyPictures, String locationTitle, double latitude,
                     double longitude, String createTime, String destroyTime, Boolean anonymous,
                     int likeNum, int opposeNum) {
        this.userInfo = userInfo;
        this.storyId = storyId;
        this.content = content;
        this.cover = cover;
        this.storyPictures = storyPictures;
        this.locationTitle = locationTitle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createTime = createTime;
        this.destroyTime = destroyTime;
        this.anonymous = anonymous;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
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
        if (storyPictures != null && storyPictures.size() > 0)
            return storyPictures.get(0);
        return null;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getStoryPictures() {
        return storyPictures;
    }

    public void setStoryPictures(List<String> storyPictures) {
        this.storyPictures = storyPictures;
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

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
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
        dest.writeStringList(this.storyPictures);
        dest.writeString(this.locationTitle);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.createTime);
        dest.writeString(this.destroyTime);
        dest.writeValue(this.anonymous);
        dest.writeInt(this.likeNum);
        dest.writeInt(this.opposeNum);
    }

    protected StoryBean(Parcel in) {
        this.userInfo = in.readParcelable(BaseUserInfo.class.getClassLoader());
        this.storyId = in.readString();
        this.content = in.readString();
        this.cover = in.readString();
        this.storyPictures = in.createStringArrayList();
        this.locationTitle = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.createTime = in.readString();
        this.destroyTime = in.readString();
        this.anonymous = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.likeNum = in.readInt();
        this.opposeNum = in.readInt();
    }

    public static final Creator<StoryBean> CREATOR = new Creator<StoryBean>() {
        @Override
        public StoryBean createFromParcel(Parcel source) {
            return new StoryBean(source);
        }

        @Override
        public StoryBean[] newArray(int size) {
            return new StoryBean[size];
        }
    };
}
