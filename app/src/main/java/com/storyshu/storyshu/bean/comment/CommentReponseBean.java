package com.storyshu.storyshu.bean.comment;

import com.storyshu.storyshu.bean.BaseResponseBean;

import java.util.List;

/**
 * 获取评论的返回值
 * Created by bear on 2017/5/15.
 */

public class CommentReponseBean extends BaseResponseBean {
    private List<CommentBean> data;

    public CommentReponseBean() {
    }

    public CommentReponseBean(int code, String message) {
        super(code, message);
    }

    public CommentReponseBean(List<CommentBean> data) {
        this.data = data;
    }

    public CommentReponseBean(int code, String message, List<CommentBean> data) {
        super(code, message);
        this.data = data;
    }

    public List<CommentBean> getData() {
        return data;
    }

    public void setData(List<CommentBean> data) {
        this.data = data;
    }
}
