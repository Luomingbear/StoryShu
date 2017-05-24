package com.storyshu.storyshu.bean.message;

import com.storyshu.storyshu.bean.BaseResponseBean;

import java.util.List;

/**
 * 获取用户故事未读评论的返回值
 * <p>
 * Created by bear on 2017/5/24.
 */

public class StoryCommentResponseBean extends BaseResponseBean {
    private List<StoryCommentBean> data;

    public StoryCommentResponseBean() {
    }

    public StoryCommentResponseBean(List<StoryCommentBean> data) {
        this.data = data;
    }

    public StoryCommentResponseBean(int code, String message, List<StoryCommentBean> data) {
        super(code, message);
        this.data = data;
    }

    public List<StoryCommentBean> getData() {
        return data;
    }

    public void setData(List<StoryCommentBean> data) {
        this.data = data;
    }
}
