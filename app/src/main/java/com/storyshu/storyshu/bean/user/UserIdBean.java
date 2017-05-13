package com.storyshu.storyshu.bean.user;

/**
 * 存放用户的id，用于搜索用户信息
 * Created by bear on 2017/5/11.
 */

public class UserIdBean {
    private int userId;

    public UserIdBean() {
    }

    public UserIdBean(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
