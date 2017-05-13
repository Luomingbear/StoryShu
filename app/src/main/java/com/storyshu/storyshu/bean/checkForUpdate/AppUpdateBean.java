package com.storyshu.storyshu.bean.checkForUpdate;

/**
 * 软件的更新信息
 * Created by bear on 2017/5/13.
 */

public class AppUpdateBean {
    private float version;
    private String title;
    private String description;
    private String path;

    public AppUpdateBean() {
    }

    public AppUpdateBean(float version, String title, String description, String path) {
        this.version = version;
        this.title = title;
        this.description = description;
        this.path = path;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
