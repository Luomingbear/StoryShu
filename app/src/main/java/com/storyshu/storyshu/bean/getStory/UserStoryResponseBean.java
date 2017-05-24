package com.storyshu.storyshu.bean.getStory;

import com.storyshu.storyshu.bean.BaseResponseBean;
import com.storyshu.storyshu.info.CardInfo;

import java.util.List;

/**
 * 获取用户的故事的返回值
 * Created by bear on 2017/5/23.
 */

public class UserStoryResponseBean extends BaseResponseBean {
    private List<CardInfo> data;

    public UserStoryResponseBean() {
    }

    public UserStoryResponseBean(int code, String message) {
        super(code, message);
    }

    public UserStoryResponseBean(List<CardInfo> data) {
        this.data = data;
    }

    public UserStoryResponseBean(int code, String message, List<CardInfo> data) {
        super(code, message);
        this.data = data;
    }

    public List<CardInfo> getData() {
        return data;
    }

    public void setData(List<CardInfo> data) {
        this.data = data;
    }
}
