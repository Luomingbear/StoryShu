package com.storyshu.storyshu.bean.getStory;

import android.os.Parcel;
import android.os.Parcelable;

import com.storyshu.storyshu.info.BaseUserInfo;
import com.storyshu.storyshu.info.CardInfo;

import java.util.List;

/**
 * 一个故事包含的完整信息
 * Created by bear on 2017/5/12.
 */

public class StoryBean extends CardInfo implements Parcelable {
    private List<String> storyPictures;

    public StoryBean() {
    }

    public StoryBean(List<String> storyPictures, int likeNum, int opposeNum) {
        this.storyPictures = storyPictures;
    }

    public StoryBean(BaseUserInfo userInfo, String storyId, String content, String cover, String locationTitle, double latitude, double longitude, String createTime, String destroyTime, Boolean anonymous, Boolean like, Boolean oppose, String title, int storyType, int likeNum, int opposeNum, int commentNum, List<String> storyPictures) {
        super(userInfo, storyId, content, cover, locationTitle, latitude, longitude, createTime, destroyTime, anonymous, like, oppose, title, storyType, likeNum, opposeNum, commentNum);
        this.storyPictures = storyPictures;
    }

    public StoryBean(Parcel in, List<String> storyPictures) {
        super(in);
        this.storyPictures = storyPictures;
    }

    public StoryBean(CardInfo cardInfo) {
        super(cardInfo.getUserInfo(), cardInfo.getStoryId(), cardInfo.getContent(), cardInfo.getCover(),
                cardInfo.getLocationTitle(), cardInfo.getLatitude(), cardInfo.getLongitude(), cardInfo.getCreateTime(),
                cardInfo.getDestroyTime(), cardInfo.getAnonymous(), cardInfo.getLike(), cardInfo.getOppose(),
                cardInfo.getTitle(), cardInfo.getStoryType(), cardInfo.getLikeNum(), cardInfo.getOpposeNum(), cardInfo.getCommentNum());
    }


    public List<String> getStoryPictures() {
        return storyPictures;
    }

    public void setStoryPictures(List<String> storyPictures) {
        this.storyPictures = storyPictures;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeStringList(this.storyPictures);
    }

    protected StoryBean(Parcel in) {
        super(in);
        this.storyPictures = in.createStringArrayList();
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

