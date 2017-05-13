package com.storyshu.storyshu.bean.getStory;

import com.storyshu.storyshu.bean.BaseResponseBean;

/**
 * 获取故事详情的时候的返回值
 * Created by bear on 2017/5/12.
 */

public class StoryReponseBean extends BaseResponseBean {
    private StoryBean data;

    public StoryReponseBean() {
    }

    public StoryReponseBean(int code, String message) {
        super(code, message);
    }

    public StoryReponseBean(StoryBean data) {
        this.data = data;
    }

    public StoryReponseBean(int code, String message, StoryBean data) {
        super(code, message);
        this.data = data;
    }

    public StoryBean getData() {
        return data;
    }

    public void setData(StoryBean data) {
        this.data = data;
    }
}
