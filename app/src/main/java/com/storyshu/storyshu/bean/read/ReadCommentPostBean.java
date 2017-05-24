package com.storyshu.storyshu.bean.read;

import java.util.List;

/**
 * 上传已读评论数据
 * Created by bear on 2017/5/25.
 */

public class ReadCommentPostBean {
    private List<String> readInfo; //commentId 列表

    public ReadCommentPostBean() {
    }

    public ReadCommentPostBean(List<String> readInfo) {
        this.readInfo = readInfo;
    }

    public List<String> getReadInfo() {
        return readInfo;
    }

    public void setReadInfo(List<String> readInfo) {
        this.readInfo = readInfo;
    }
}
