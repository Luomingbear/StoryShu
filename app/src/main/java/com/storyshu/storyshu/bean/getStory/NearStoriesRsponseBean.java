package com.storyshu.storyshu.bean.getStory;

import com.storyshu.storyshu.bean.BaseResponseBean;

import java.util.List;

/**
 * 获取附近的故事时返回的值
 * Created by bear on 2017/5/12.
 */

public class NearStoriesRsponseBean extends BaseResponseBean {
    private List<StoryBean> data;

    public NearStoriesRsponseBean() {
    }

    public NearStoriesRsponseBean(int code, String message) {
        super(code, message);
    }

    public NearStoriesRsponseBean(List<StoryBean> data) {
        this.data = data;
    }

    public NearStoriesRsponseBean(int code, String message, List<StoryBean> data) {
        super(code, message);
        this.data = data;
    }

    public List<StoryBean> getData() {
        return data;
    }

    public void setData(List<StoryBean> data) {
        this.data = data;
    }
}
