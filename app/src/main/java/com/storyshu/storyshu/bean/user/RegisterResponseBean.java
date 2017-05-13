package com.storyshu.storyshu.bean.user;

import com.storyshu.storyshu.info.BaseUserInfo;

/**
 * 注册返回值
 * Created by bear on 2017/5/10.
 */

public class RegisterResponseBean {
    private int code = 500;
    private String message;
    private BaseUserInfo data;

    public RegisterResponseBean() {
    }

    public RegisterResponseBean(int code, String message, BaseUserInfo data) {
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
