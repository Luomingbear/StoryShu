package com.storyshu.storyshu.bean;

import java.util.List;

/**
 * 获取故事配图的返回值
 * Created by bear on 2017/5/24.
 */

public class StoryPicResponseBean extends BaseResponseBean {
    private List<String> data;

    public StoryPicResponseBean() {
    }

    public StoryPicResponseBean(int code, String message) {
        super(code, message);
    }

    public StoryPicResponseBean(List<String> data) {
        this.data = data;
    }

    public StoryPicResponseBean(int code, String message, List<String> data) {
        super(code, message);
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
