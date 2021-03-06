package com.storyshu.storyshu.bean.getStory;

import com.storyshu.storyshu.bean.BaseResponseBean;
import com.storyshu.storyshu.info.CardInfo;

import java.util.List;

/**
 * 获取附近的故事时返回的值
 * Created by bear on 2017/5/12.
 */

public class NearStoriesRsponseBean extends BaseResponseBean {
    private List<CardInfo> data;

    public NearStoriesRsponseBean() {
    }

    public NearStoriesRsponseBean(int code, String message) {
        super(code, message);
    }

    public NearStoriesRsponseBean(List<CardInfo> data) {
        this.data = data;
    }

    public NearStoriesRsponseBean(int code, String message, List<CardInfo> data) {
        super(code, message);
        this.data = data;
    }

    public List<CardInfo> getData() {
        return data;
    }

    public void setData(List<CardInfo> data) {
        this.data = data;
    }
}
