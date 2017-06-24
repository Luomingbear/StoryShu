package com.storyshu.storyshu.info;

/**
 * 文章的数据结构
 * Created by bear on 2017/6/20.
 */

public class LongStoryInfo {
    private String title; //标题
    private String content; //文章的具体内容，经过编码之后的
    private String createTime;
    private String location;
    private double latitude;
    private double longitude;

    private Boolean anonymous;
    private Boolean like = false; //点赞
    private Boolean oppose = false; //反对

    private int likeNum;
    private int opposeNum;
    private int commentNum;

    public LongStoryInfo(String title, String content, String createTime, String location, double latitude, double longitude, Boolean anonymous, Boolean like, Boolean oppose, int likeNum, int opposeNum, int commentNum) {
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.anonymous = anonymous;
        this.like = like;
        this.oppose = oppose;
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
        this.commentNum = commentNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public Boolean getOppose() {
        return oppose;
    }

    public void setOppose(Boolean oppose) {
        this.oppose = oppose;
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

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }
}

