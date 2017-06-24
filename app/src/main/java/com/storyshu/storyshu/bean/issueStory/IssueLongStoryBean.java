package com.storyshu.storyshu.bean.issueStory;

/**
 * 发布的文章的数据结构
 * Created by bear on 2017/6/20.
 */

public class IssueLongStoryBean extends BaseIssueStoryBean {

    private String title; //标题

    public IssueLongStoryBean() {
    }

    public IssueLongStoryBean(String title) {
        this.title = title;
    }

    public IssueLongStoryBean(int userId, String content, String cityName, String locationTitle, double latitude, double longitude, String createTime, Boolean isAnonymous, int tag, String title) {
        super(userId, content, cityName, locationTitle, latitude, longitude, createTime, isAnonymous, tag);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

