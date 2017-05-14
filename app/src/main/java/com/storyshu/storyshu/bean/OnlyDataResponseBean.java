package com.storyshu.storyshu.bean;

/**
 * 创建故事的返回值
 * Created by bear on 2017/5/11.
 */

public class OnlyDataResponseBean extends BaseResponseBean {
    private String data;

    public OnlyDataResponseBean() {
    }

    public OnlyDataResponseBean(int code, String message) {
        super(code, message);
    }

    public OnlyDataResponseBean(String data) {
        this.data = data;
    }

    public OnlyDataResponseBean(int code, String message, String data) {
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
