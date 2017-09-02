package com.storyshu.storyshu.mvp.main;

/**
 * 主页的逻辑接口
 * Created by bear on 2017/5/13.
 */

public interface MainPresenter {

    /**
     * 检查更新软件
     */
    void checkForUpdate();

    /**
     * 下载最新的软件
     */
    void downloadNewApp();

    void addConnectListener();
}
