package com.storyshu.storyshu.bean.comment;

import com.storyshu.storyshu.bean.BaseResponseBean;

/**
 * 获取评论数量
 * Created by bear on 2017/5/15.
 */

public class CommentSizeResponseBean extends BaseResponseBean {
    private int data;

    public CommentSizeResponseBean() {
    }

    public CommentSizeResponseBean(int code, String message) {
        super(code, message);
    }

    public CommentSizeResponseBean(int data) {
        this.data = data;
    }

    public CommentSizeResponseBean(int code, String message, int data) {
        super(code, message);
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
