package com.storyshu.storyshu.model.stories;

import com.storyshu.storyshu.bean.StoryPicResponseBean;
import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.utils.net.CodeUtil;
import com.storyshu.storyshu.utils.net.RetrofitManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 故事配图的model
 * Created by bear on 2017/5/24.
 */

public class StoryPicModel {
    private OnStoryPicGotListener onStoryPicGotListener;

    public void setOnStoryPicGotListener(OnStoryPicGotListener onStoryPicGotListener) {
        this.onStoryPicGotListener = onStoryPicGotListener;
    }

    public interface OnStoryPicGotListener {
        void onSucceed(List<String> picList);

        void onFailed(String error);
    }

    public StoryPicModel() {
    }

    /**
     * 获取故事的配图信息
     *
     * @param storyId
     */
    public void getStoryPic(String storyId) {
        Call<StoryPicResponseBean> call = RetrofitManager.getInstance().getService().getStoryPic(new StoryIdBean(storyId));
        call.enqueue(new Callback<StoryPicResponseBean>() {
            @Override
            public void onResponse(Call<StoryPicResponseBean> call, Response<StoryPicResponseBean> response) {
                if (response.body().getCode() == CodeUtil.Succeed) {
                    if (onStoryPicGotListener != null)
                        onStoryPicGotListener.onSucceed(response.body().getData());
                } else {
                    if (onStoryPicGotListener != null)
                        onStoryPicGotListener.onFailed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<StoryPicResponseBean> call, Throwable t) {
                if (onStoryPicGotListener != null)
                    onStoryPicGotListener.onFailed(t.getMessage());
            }
        });

    }
}
