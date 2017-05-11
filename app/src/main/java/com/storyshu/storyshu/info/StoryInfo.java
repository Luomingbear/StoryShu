package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;
import com.storyshu.storyshu.utils.ListUtil;

import java.util.List;

/**
 * 故事信息
 * Created by bear on 2016/12/27.
 */

public class StoryInfo implements Parcelable {
    private String storyId; //故事的id
    private String content; //故事的内容
    private String cover; //故事的封面图
    private List<String> storyPic; //配图
    private BaseUserInfo userInfo; //用户信息
    private String createTime; //发布时间
    private String destroyTime; //保存时间，单位分钟
    private String Content; //正文
    private LatLng latLng; //坐标
    private String location; //地点描述

    private boolean isAnonymous = false; //是否匿名


    public StoryInfo() {
    }

    public StoryInfo(String storyId, String cover, List<String> storyPic, String content,
                     BaseUserInfo userInfo, String createDate, String destroyTime, String content1,
                     LatLng latLng, String location, boolean isAnonymous) {
        this.storyId = storyId;
        this.cover = cover;
        this.storyPic = storyPic;
        this.content = content;
        this.userInfo = userInfo;
        this.createTime = createDate;
        this.destroyTime = destroyTime;
        Content = content1;
        this.latLng = latLng;
        this.location = location;
        this.isAnonymous = isAnonymous;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getStoryPic() {
        return storyPic;
    }

    public void setStoryPic(List<String> storyPic) {
        this.storyPic = storyPic;
    }

    public void setStoryPic(String storyPic) {
        this.storyPic = ListUtil.StringToStringList(storyPic);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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

    public BaseUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BaseUserInfo userInfo) {
        this.userInfo = userInfo;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.storyId);
        dest.writeString(this.content);
        dest.writeString(this.cover);
        dest.writeStringList(this.storyPic);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeString(this.createTime);
        dest.writeString(this.destroyTime);
        dest.writeString(this.Content);
        dest.writeParcelable(this.latLng, flags);
        dest.writeString(this.location);
        dest.writeByte(this.isAnonymous ? (byte) 1 : (byte) 0);
    }

    protected StoryInfo(Parcel in) {
        this.storyId = in.readString();
        this.content = in.readString();
        this.cover = in.readString();
        this.storyPic = in.createStringArrayList();
        this.userInfo = in.readParcelable(BaseUserInfo.class.getClassLoader());
        this.createTime = in.readString();
        this.destroyTime = in.readString();
        this.Content = in.readString();
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.location = in.readString();
        this.isAnonymous = in.readByte() != 0;
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
