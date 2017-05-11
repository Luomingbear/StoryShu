package com.storyshu.storyshu.bean;

/**
 * 创建故事的返回值
 * Created by bear on 2017/5/11.
 */

public class IssuseResponseBean extends BaseResponseBean {
    private String data;

    public IssuseResponseBean() {
    }

    public IssuseResponseBean(int code, String message) {
        super(code, message);
    }

    public IssuseResponseBean(String data) {
        this.data = data;
    }

    public IssuseResponseBean(int code, String message, String data) {
        super(code, message);
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
