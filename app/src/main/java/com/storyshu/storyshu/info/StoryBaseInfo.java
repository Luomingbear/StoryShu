package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 故事的基本信息
 * Created by bear on 2016/12/25.
 */

public class StoryBaseInfo implements Parcelable {
    private String title; //故事的标题
    private String extract; //故事的摘要
    private String content; //故事的正文

    public StoryBaseInfo() {
    }

    public StoryBaseInfo(String title, String extract, String content) {
        this.title = title;
        this.extract = extract;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (TextUtils.isEmpty(content))
            return;
        this.content = content;
        //摘要最多25个字
        int length = Math.min(content.length(), 25);
        this.extract = content.substring(0, length);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.extract);
        dest.writeString(this.content);
    }

    protected StoryBaseInfo(Parcel in) {
        this.title = in.readString();
        this.extract = in.readString();
        this.content = in.readString();
    }

    public static final Creator<StoryBaseInfo> CREATOR = new Creator<StoryBaseInfo>() {
        @Override
        public StoryBaseInfo createFromParcel(Parcel source) {
            return new StoryBaseInfo(source);
        }

        @Override
        public StoryBaseInfo[] newArray(int size) {
            return new StoryBaseInfo[size];
        }
    };
}
