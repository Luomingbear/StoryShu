package com.storyshu.storyshu.bean.message;

import com.storyshu.storyshu.bean.BaseResponseBean;

import java.util.List;

/**
 * 获取故事的赞的数据返回值
 * Created by bear on 2017/5/24.
 */

public class StoryLikeResponseBean extends BaseResponseBean {
    private List<StoryLikeBean> data;

    public StoryLikeResponseBean() {
    }

    public StoryLikeResponseBean(int code, String message) {
        super(code, message);
    }

    public StoryLikeResponseBean(List<StoryLikeBean> data) {
        this.data = data;
    }

    public StoryLikeResponseBean(int code, String message, List<StoryLikeBean> data) {
        super(code, message);
        this.data = data;
    }

    public List<StoryLikeBean> getData() {
        return data;
    }

    public void setData(List<StoryLikeBean> data) {
        this.data = data;
    }
}
