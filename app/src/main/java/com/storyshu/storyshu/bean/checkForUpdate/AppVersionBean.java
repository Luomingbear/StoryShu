package com.storyshu.storyshu.bean.checkForUpdate;

/**
 * 软件的版本号
 * Created by bear on 2017/5/13.
 */

public class AppVersionBean {
    private float version;

    public AppVersionBean() {
    }

    public AppVersionBean(float version) {
        this.version = version;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }
}
