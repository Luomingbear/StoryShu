package com.storyshu.storyshu.bean.checkForUpdate;

import com.storyshu.storyshu.bean.BaseResponseBean;

/**
 * 获取最新的版本信息返回值
 * Created by bear on 2017/5/13.
 */

public class VersionResponseBean extends BaseResponseBean {
    private AppUpdateBean data;

    public VersionResponseBean() {
    }

    public VersionResponseBean(int code, String message) {
        super(code, message);
    }

    public VersionResponseBean(AppUpdateBean data) {
        this.data = data;
    }

    public VersionResponseBean(int code, String message, AppUpdateBean data) {
        super(code, message);
        this.data = data;
    }

    public AppUpdateBean getData() {
        return data;
    }

    public void setData(AppUpdateBean data) {
        this.data = data;
    }
}
