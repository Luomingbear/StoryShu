package com.storyshu.storyshu.bean.issueStory;

import java.util.List;

/**
 * 创建故事时需要上传的数据
 * Created by bear on 2017/5/11.
 */

public class IssueStoryBean extends BaseIssueStoryBean {
    private String cover; //故事的封面图
    private List<String> storyPictures; //配图

    public IssueStoryBean() {
    }

    public IssueStoryBean(String cover, List<String> storyPictures) {
        this.cover = cover;
        this.storyPictures = storyPictures;
    }

    public IssueStoryBean(int userId, String content, String cityName, String locationTitle, double latitude, double longitude, String createTime, String destroyTime, Boolean isAnonymous,
                          int storyType, String cover, List<String> storyPictures) {
        super(userId, content, cityName, locationTitle, latitude, longitude, createTime, destroyTime, isAnonymous, storyType);
        this.cover = cover;
        this.storyPictures = storyPictures;
    }

    public List<String> getStoryPictures() {
        return storyPictures;
    }

    public void setStoryPictures(List<String> storyPictures) {
        this.storyPictures = storyPictures;
    }

    public String getCover() {
        if (storyPictures != null && storyPictures.size() > 0)
            return storyPictures.get(0);

        return null;
    }

    public void setCover(String cover) {
        if (storyPictures != null && storyPictures.size() > 0)
            this.cover = storyPictures.get(0);
    }

}
