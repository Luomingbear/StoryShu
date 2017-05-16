package com.storyshu.storyshu.info;

import android.os.Parcel;

import com.storyshu.storyshu.bean.getStory.StoryBean;

import java.util.List;

/**
 * 候机厅推送的故事信息结构
 * Created by bear on 2017/3/19.
 */

public class AirPortPushInfo extends CardInfo {
    private int pushType = TYPE_STORY; //类型ad：广告 story：故事
    private String adUrl; //广告的网络链接


    public final static int TYPE_STORY = 0;
    public final static int TYPE_AD = TYPE_STORY + 1;

    public AirPortPushInfo() {
    }

    public AirPortPushInfo(int pushType, String adUrl) {
        this.pushType = pushType;
        this.adUrl = adUrl;
    }

    public AirPortPushInfo(boolean like, boolean oppose, int pushType, String adUrl) {
        super(like, oppose);
        this.pushType = pushType;
        this.adUrl = adUrl;
    }

    public AirPortPushInfo(StoryBean storyBean, int pushType, String adUrl) {
        super(storyBean);
        this.pushType = pushType;
        this.adUrl = adUrl;
    }

    public AirPortPushInfo(BaseUserInfo userInfo, String storyId, String content, String cover, List<String> storyPictures, String locationTitle, double latitude, double longitude, String createTime, String destroyTime, Boolean anonymous, int likeNum, int opposeNum, boolean like, boolean oppose, int pushType, String adUrl) {
        super(userInfo, storyId, content, cover, storyPictures, locationTitle, latitude, longitude, createTime, destroyTime, anonymous, likeNum, opposeNum, like, oppose);
        this.pushType = pushType;
        this.adUrl = adUrl;
    }

    public AirPortPushInfo(Parcel in, int pushType, String adUrl) {
        super(in);
        this.pushType = pushType;
        this.adUrl = adUrl;
    }

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.pushType);
        dest.writeString(this.adUrl);
    }

    protected AirPortPushInfo(Parcel in) {
        super(in);
        this.pushType = in.readInt();
        this.adUrl = in.readString();
    }

    public static final Creator<AirPortPushInfo> CREATOR = new Creator<AirPortPushInfo>() {
        @Override
        public AirPortPushInfo createFromParcel(Parcel source) {
            return new AirPortPushInfo(source);
        }

        @Override
        public AirPortPushInfo[] newArray(int size) {
            return new AirPortPushInfo[size];
        }
    };
}
