package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户数据信息
 * Created by bear on 2016/12/13.
 */

public class BaseUserInfo implements Parcelable {

    private String nickname; //昵称
    private String userId; //用户id 默认游客身份
    private String avatar; //头像

    public BaseUserInfo() {
    }

    public BaseUserInfo(String nickname, String userId, String avatar) {
        this.nickname = nickname;
        this.userId = userId;
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nickname);
        dest.writeString(this.userId);
        dest.writeString(this.avatar);
    }

    protected BaseUserInfo(Parcel in) {
        this.nickname = in.readString();
        this.userId = in.readString();
        this.avatar = in.readString();
    }

    public static final Creator<BaseUserInfo> CREATOR = new Creator<BaseUserInfo>() {
        @Override
        public BaseUserInfo createFromParcel(Parcel source) {
            return new BaseUserInfo(source);
        }

        @Override
        public BaseUserInfo[] newArray(int size) {
            return new BaseUserInfo[size];
        }
    };
}
