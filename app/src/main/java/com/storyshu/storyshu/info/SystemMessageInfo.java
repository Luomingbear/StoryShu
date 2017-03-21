package com.storyshu.storyshu.info;

/**
 * 系统消息的信息格式
 * Created by bear on 2017/3/21.
 */

public class SystemMessageInfo {
    private String url; //链接地址
    private String describe; //文本描述
    private String createTime; //推送的时间

    public SystemMessageInfo() {
    }

    public SystemMessageInfo(String url, String describe, String createTime) {
        this.url = url;
        this.describe = describe;
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
