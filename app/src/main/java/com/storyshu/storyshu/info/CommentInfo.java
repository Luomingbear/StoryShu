package com.storyshu.storyshu.info;

import android.os.Parcel;

/**
 * 评论信息
 * Created by bear on 2017/3/15.
 */

public class CommentInfo extends BaseUserInfo {
    private String createTime = ""; //评论发布的时间
    private int likeNum = 0; //点赞的数量
    private int opposeNum = 0; //喝倒彩的数量
    private String tag = ""; //标记 hot／楼层
    private String comment = ""; //评论

    public CommentInfo() {
    }

    public CommentInfo(String nickname, String userId, String avatar, String createTime,
                       int likeNum, int opposeNum, String tag, String comment) {
        super(nickname, userId, avatar);
        this.createTime = createTime;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
        this.tag = tag;
        this.comment = comment;
    }

    public CommentInfo(String createTime, int likeNum, int opposeNum, String tag, String comment) {
        this.createTime = createTime;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
        this.tag = tag;
        this.comment = comment;
    }

    public CommentInfo(Parcel in, String createTime, int likeNum, int opposeNum, String tag, String comment) {
        super(in);
        this.createTime = createTime;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
        this.tag = tag;
        this.comment = comment;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getOpposeNum() {
        return opposeNum;
    }

    public void setOpposeNum(int opposeNum) {
        this.opposeNum = opposeNum;
    }

    public String getTags() {
        return tag;
    }

    public void setTags(String tag) {
        this.tag = tag;
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
        super.writeToParcel(dest, flags);
        dest.writeString(this.createTime);
        dest.writeInt(this.likeNum);
        dest.writeInt(this.opposeNum);
        dest.writeString(this.tag);
        dest.writeString(this.comment);
    }

    protected CommentInfo(Parcel in) {
        super(in);
        this.createTime = in.readString();
        this.likeNum = in.readInt();
        this.opposeNum = in.readInt();
        this.tag = in.readString();
        this.comment = in.readString();
    }

    public static final Creator<CommentInfo> CREATOR = new Creator<CommentInfo>() {
        @Override
        public CommentInfo createFromParcel(Parcel source) {
            return new CommentInfo(source);
        }

        @Override
        public CommentInfo[] newArray(int size) {
            return new CommentInfo[size];
        }
    };
}
