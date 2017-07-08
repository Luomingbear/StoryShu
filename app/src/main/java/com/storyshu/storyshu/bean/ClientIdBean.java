package com.storyshu.storyshu.bean;

/**
 * Created by bear on 2017/7/8.
 */

public class ClientIdBean {
    private String clientId;
    private int userId;

    public ClientIdBean() {

    }

    public ClientIdBean(String clientId, int userId) {
        this.clientId = clientId;
        this.userId = userId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
