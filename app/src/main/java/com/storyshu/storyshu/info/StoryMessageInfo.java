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
    private String storyId; //故事id
    private String id; //消息id
    private String storyContent; //故事的内容
    private String cover; //故事的第一张图片
    private String comment; //评论
    private MessageType messageType = MessageType.LIKE_STORY; //类型
    private int unReadNum = 0; //未读信息

    public enum MessageType {
        LIKE_STORY,
        LIKE_COMMENT,
        COMMENT,
        SYSTEM
    }

    public StoryMessageInfo() {
    }

    public StoryMessageInfo(BaseUserInfo userInfo, String createTime, String storyId,
                            String commentId, String storyContent, String cover,
                            String comment, MessageType messageType, int unReadNum) {
        this.userInfo = userInfo;
        this.createTime = createTime;
        this.storyId = storyId;
        this.id = commentId;
        this.storyContent = storyContent;
        this.cover = cover;
        this.comment = comment;
        this.messageType = messageType;
        this.unReadNum = unReadNum;
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

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
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

    public String getComment() {
        return comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public int getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(int unReadNum) {
        this.unReadNum = unReadNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userInfo, flags);
        dest.writeString(this.createTime);
        dest.writeString(this.storyId);
        dest.writeString(this.storyContent);
        dest.writeString(this.cover);
        dest.writeString(this.comment);
        dest.writeInt(this.messageType == null ? -1 : this.messageType.ordinal());
    }

    protected StoryMessageInfo(Parcel in) {
        this.userInfo = in.readParcelable(BaseUserInfo.class.getClassLoader());
        this.createTime = in.readString();
        this.storyId = in.readString();
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
