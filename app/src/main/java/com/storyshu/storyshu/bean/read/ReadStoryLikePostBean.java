package com.storyshu.storyshu.bean.read;

import java.util.List;

/**
 * 标记故事点赞为已读的上传数据
 * Created by bear on 2017/5/25.
 */

public class ReadStoryLikePostBean {
    private List<String> readInfo;

    public ReadStoryLikePostBean() {
    }

    public ReadStoryLikePostBean(List<String> readInfo) {
        this.readInfo = readInfo;
    }

    public List<String> getReadInfo() {
        return readInfo;
    }

    public void setReadInfo(List<String> readInfo) {
        this.readInfo = readInfo;
    }
}
