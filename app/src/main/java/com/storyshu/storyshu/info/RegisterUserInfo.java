package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 注册账号使用的用户信息
 * Created by bear on 2017/3/29.
 */

public class RegisterUserInfo implements Parcelable {
    private String email; //邮箱
    private String password; //密码

    private String nickname; //昵称
    private String avatar; //头像

    public RegisterUserInfo() {

    }

    public RegisterUserInfo(String email, String password, String nickname, String avatar) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        String ss = "eamil:" + getEmail() + "\n"
                + "password:" + getPassword() + "\n"
                + "nickname:" + getNickname() + "\n"
                + "avatar:" + getAvatar() + "\n";
        return ss;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
    }

    protected RegisterUserInfo(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
        this.nickname = in.readString();
        this.avatar = in.readString();
    }

    public static final Creator<RegisterUserInfo> CREATOR = new Creator<RegisterUserInfo>() {
        @Override
        public RegisterUserInfo createFromParcel(Parcel source) {
            return new RegisterUserInfo(source);
        }

        @Override
        public RegisterUserInfo[] newArray(int size) {
            return new RegisterUserInfo[size];
        }
    };
}
