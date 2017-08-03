package com.storyshu.storyshu.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 下载
 * Created by bear on 2017/7/31.
 */

@Entity
public class KTDownload {
    @Id(autoincrement = true)
    private Long download_id;

    @Property(nameInDb = "download_url")
    private String downloadUrl = ""; //下载的网络地址

    @Property(nameInDb = "save_path")
    private String savePath = ""; //保存的本地地址

    @Property(nameInDb = "downloaded_size")
    private long downloadSize = 0; //已经下载的位置

    @Property(nameInDb = "cur_thread")
    private int curThread = 0; //当前是第几个下载线程

    @Property(nameInDb = "is_completed")
    private boolean isCompleted = false; //下载是否完成

    @Generated(hash = 738035972)
    public KTDownload(Long download_id, String downloadUrl, String savePath,
            long downloadSize, int curThread, boolean isCompleted) {
        this.download_id = download_id;
        this.downloadUrl = downloadUrl;
        this.savePath = savePath;
        this.downloadSize = downloadSize;
        this.curThread = curThread;
        this.isCompleted = isCompleted;
    }

    @Generated(hash = 311529483)
    public KTDownload() {
    }

    public Long getDownload_id() {
        return this.download_id;
    }

    public void setDownload_id(Long download_id) {
        this.download_id = download_id;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getSavePath() {
        return this.savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getDownloadSize() {
        return this.downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public int getCurThread() {
        return this.curThread;
    }

    public void setCurThread(int curThread) {
        this.curThread = curThread;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

}
