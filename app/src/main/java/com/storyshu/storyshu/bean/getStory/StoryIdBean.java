package com.storyshu.storyshu.bean.getStory;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 包含故事id的bean
 * 用于请求故事信息
 * Created by bear on 2017/5/12.
 */

public class StoryIdBean implements Parcelable {
    private String storyId;

    public StoryIdBean() {
    }

    public StoryIdBean(String storyId) {
        this.storyId = storyId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.storyId);
    }

    protected StoryIdBean(Parcel in) {
        this.storyId = in.readString();
    }

    public static final Parcelable.Creator<StoryIdBean> CREATOR = new Parcelable.Creator<StoryIdBean>() {
        @Override
        public StoryIdBean createFromParcel(Parcel source) {
            return new StoryIdBean(source);
        }

        @Override
        public StoryIdBean[] newArray(int size) {
            return new StoryIdBean[size];
        }
    };
}
