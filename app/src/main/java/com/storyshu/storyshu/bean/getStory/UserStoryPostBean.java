package com.storyshu.storyshu.bean.getStory;

/**
 * 请求用户的故事的请求数据
 * Created by bear on 2017/5/23.
 */

public class UserStoryPostBean {
    private int userId;
    protected int page = 1; //第几页，用于加载更多

    public UserStoryPostBean() {
    }

    public UserStoryPostBean(int userId, int start) {
        this.userId = userId;
        this.page = start;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
