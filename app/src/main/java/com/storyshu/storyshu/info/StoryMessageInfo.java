package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 信息页面
 * 列表的每一个条目的信息
 * Created by bear on 2017/3/21.
 */

public class StoryMessageInfo implements Parcelable {
    private BaseUserInfo userInfo; //用户信息
    private String createTime; //创建时间
    private int storyId; //故事id
    private String storyContent; //故事的内容
    private String cover; //故事的第一张图片
    private String comment; //评论
    private MessageType messageType = MessageType.LIKE; //类型

    public enum MessageType {
        LIKE,
        COMMENT,
        SYSTEM
    }

    public StoryMessageInfo() {
    }

    public StoryMessageInfo(BaseUserInfo userInfo, String createTime, int storyId, String storyContent,
                            String cover, String comment, MessageType messageType) {
        this.userInfo = userInfo;
        this.createTime = createTime;
        this.storyId = storyId;
        this.storyContent = storyContent;
        this.cover = cover;
        this.comment = comment;
        this.messageType = messageType;
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

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getStoryContent() {
        return storyContent;
    }

    public void setStoryContent(String storyContent) {
        this.storyContent = storyContent;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userInfo, flags);
        dest.writeString(this.createTime);
        dest.writeInt(this.storyId);
        dest.writeString(this.storyContent);
        dest.writeString(this.cover);
        dest.writeString(this.comment);
        dest.writeInt(this.messageType == null ? -1 : this.messageType.ordinal());
    }

    protected StoryMessageInfo(Parcel in) {
        this.userInfo = in.readParcelable(BaseUserInfo.class.getClassLoader());
        this.createTime = in.readString();
        this.storyId = in.readInt();
        this.storyContent = in.readString();
        this.cover = in.readString();
        this.comment = in.readString();
        int tmpMessageType = in.readInt();
        this.messageType = tmpMessageType == -1 ? null : MessageType.values()[tmpMessageType];
    }

    public static final Creator<StoryMessageInfo> CREATOR = new Creator<StoryMessageInfo>() {
        @Override
        public StoryMessageInfo createFromParcel(Parcel source) {
            return new StoryMessageInfo(source);
        }

        @Override
        public StoryMessageInfo[] newArray(int size) {
            return new StoryMessageInfo[size];
        }
    };
}
