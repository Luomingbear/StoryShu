package com.storyshu.storyshu.info;

/**
 * Created by bear on 2017/4/26.
 */

public class LoginInfo {
    private String email;
    private String password;

    public LoginInfo() {
    }

    public LoginInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
