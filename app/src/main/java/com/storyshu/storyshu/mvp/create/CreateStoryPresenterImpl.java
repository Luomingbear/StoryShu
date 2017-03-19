package com.storyshu.storyshu.mvp.create;

/**
 * 写故事的代理实现
 * Created by bear on 2017/3/17.
 */

public class CreateStoryPresenterImpl implements CreateStoryPresenter {
    private CreateStoryView mCreateStoryView; //视图接口

    public CreateStoryPresenterImpl(CreateStoryView createStoryView) {
        this.mCreateStoryView = createStoryView;
    }

    @Override
    public void issueStory() {

    }

    @Override
    public void getLocationPoi() {

    }

    @Override
    public void getPicList() {

    }

    @Override
    public void setLifeTime() {

    }

}
