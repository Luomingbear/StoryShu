package com.storyshu.storyshu.bean;

/**
 * 启动页的数据格式
 * Created by bear on 2017/5/8.
 */

public class LauncherResponseBean {
    private String describe; //描述文本
    private String url; //描述文本
    private String ad; //描述文本

    public LauncherResponseBean() {
    }

    public LauncherResponseBean(String describe, String url, String ad) {
        this.describe = describe;
        this.url = url;
        this.ad = ad;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }
}
