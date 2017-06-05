package com.storyshu.storyshu.info;

/**
 * 单个聊天信息
 * Created by bear on 2017/5/25.
 */

public class DiscussItemInfo {
    private BaseUserInfo userInfo;

    private String createTime;
    private String content;
    private int DiscussType = ME; //气泡类型

    public static final int ME = 1; //显示我自己
    public static final int OTHER = ME + 1; //显示对方

    public DiscussItemInfo() {
    }

    public DiscussItemInfo(BaseUserInfo userInfo, String createTime, String content, int discussType) {
        this.userInfo = userInfo;
        this.createTime = createTime;
        this.content = content;
        DiscussType = discussType;
    }

    public int getDiscussType() {
        return DiscussType;
    }

    public void setDiscussType(int discussType) {
        DiscussType = discussType;
    }

    public BaseUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BaseUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
