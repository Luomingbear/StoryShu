package com.storyshu.storyshu.bean;

import com.storyshu.storyshu.info.BaseUserInfo;

/**
 * 用户基本信息
 * Created by bear on 2017/5/10.
 */

public class UserLoginResponseBean {
    private int code;
    private String message;
    private BaseUserInfo data;

    public UserLoginResponseBean() {
    }

    public UserLoginResponseBean(int code, String message, BaseUserInfo data) {
        this.code = code;
        this.message = message;
        this.data = data;
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

    public BaseUserInfo getData() {
        return data;
    }

    public void setData(BaseUserInfo data) {
        this.data = data;
    }
}
