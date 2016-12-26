package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户数据信息
 * Created by bear on 2016/12/13.
 */

public class UserInfo implements Parcelable {
    private String nickname; //昵称
    private int userId = -1; //用户id 默认游客身份
    private String avatar; //头像

    public UserInfo(String nickname, int userId, String headPortrait) {
        this.nickname = nickname;
        this.userId = userId;
        this.avatar = headPortrait;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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
        dest.writeInt(this.userId);
        dest.writeString(this.avatar);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.nickname = in.readString();
        this.userId = in.readInt();
        this.avatar = in.readString();
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
