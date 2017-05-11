package com.storyshu.storyshu.bean;

/**
 * 基本的返回数据
 * Created by bear on 2017/5/11.
 */

public class BaseResponseBean {
    private int code;
    private String message;

    public BaseResponseBean() {
    }

    public BaseResponseBean(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
