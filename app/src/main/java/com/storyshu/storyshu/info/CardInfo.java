package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

import com.storyshu.storyshu.bean.getStory.StoryBean;

import java.util.List;

/**
 * 故事卡片信息类
 * Created by bear on 2016/12/4.
 */

public class CardInfo extends StoryBean implements Parcelable {

    private boolean like = false; //点赞
    private boolean oppose = false; //反对

    public CardInfo() {

    }

    public CardInfo(boolean like, boolean oppose) {
        this.like = like;
        this.oppose = oppose;
    }

    public CardInfo(StoryBean storyBean) {
        super(storyBean.getUserInfo(), storyBean.getStoryId(), storyBean.getContent(), storyBean.getCover(), storyBean.getStoryPictures(),
                storyBean.getLocationTitle(), storyBean.getLatitude(), storyBean.getLongitude(), storyBean.getCreateTime(), storyBean.getDestroyTime(),
                storyBean.getAnonymous(), storyBean.getLikeNum(), storyBean.getOpposeNum());
    }

    public CardInfo(BaseUserInfo userInfo, String storyId, String content, String cover, List<String> storyPictures, String locationTitle,
                    double latitude, double longitude, String createTime, String destroyTime, Boolean anonymous, int likeNum, int opposeNum, boolean like, boolean oppose) {
        super(userInfo, storyId, content, cover, storyPictures, locationTitle, latitude, longitude, createTime, destroyTime, anonymous, likeNum, opposeNum);
        this.like = like;
        this.oppose = oppose;
    }

    public CardInfo(Parcel in, boolean like, boolean oppose) {
        super(in);
        this.like = like;
        this.oppose = oppose;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean isOppose() {
        return oppose;
    }

    public void setOppose(boolean oppose) {
        this.oppose = oppose;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.like ? (byte) 1 : (byte) 0);
        dest.writeByte(this.oppose ? (byte) 1 : (byte) 0);
    }

    protected CardInfo(Parcel in) {
        super(in);
        this.like = in.readByte() != 0;
        this.oppose = in.readByte() != 0;
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
