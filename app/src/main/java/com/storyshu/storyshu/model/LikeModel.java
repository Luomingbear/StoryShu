package com.storyshu.storyshu.model;

import android.content.Context;
import android.util.Log;

import com.storyshu.storyshu.bean.OnlyDataResponseBean;
import com.storyshu.storyshu.bean.like.LikePostBean;
import com.storyshu.storyshu.bean.message.StoryLikeBean;
import com.storyshu.storyshu.bean.message.StoryLikeResponseBean;
import com.storyshu.storyshu.bean.user.UserIdBean;
import com.storyshu.storyshu.utils.net.CodeUtil;
import com.storyshu.storyshu.utils.net.RetrofitManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 喜欢和讨厌的实现
 * Created by bear on 2017/5/14.
 */

public class LikeModel {
    private static final String TAG = "LikeModel";
    private Context mContext;
    private OnLikeListener onLikeListener;
    private OnGotStoryLikeListener onGotStoryLikeListener;

    public interface OnLikeListener {
        void onSucceed();

        void onFailed(String error);
    }

    public interface OnGotStoryLikeListener {
        void onSucceed(List<StoryLikeBean> storyLikeBeanList);

        void onFailed(String error);
    }

    public void setOnGotStoryLikeListener(OnGotStoryLikeListener onGotStoryLikeListener) {
        this.onGotStoryLikeListener = onGotStoryLikeListener;
    }

    public void setOnLikeListener(OnLikeListener onLikeListener) {
        this.onLikeListener = onLikeListener;
    }

    public LikeModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 喜欢故事
     */
    public void likeStory(LikePostBean likePostBean) {
        Call<OnlyDataResponseBean> call = RetrofitManager.getInstance().getService().likeStory(likePostBean);
        call.enqueue(new Callback<OnlyDataResponseBean>() {
            @Override
            public void onResponse(Call<OnlyDataResponseBean> call, Response<OnlyDataResponseBean> response) {
                Log.i(TAG, "onResponse: " + response.body());
                if (response.body().getCode() == CodeUtil.Succeed) {
                    if (onLikeListener != null)
                        onLikeListener.onSucceed();
                } else {
                    if (onLikeListener != null)
                        onLikeListener.onFailed(response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<OnlyDataResponseBean> call, Throwable t) {

                if (onLikeListener != null)
                    onLikeListener.onFailed(t.getMessage());
            }
        });
    }


    /**
     * 获取用户的故事收到的赞
     * 未读的消息
     *
     * @param userId
     */
    public void getStoryLike(int userId) {
        Call<StoryLikeResponseBean> call = RetrofitManager.getInstance().getService().getStoryLike(new UserIdBean(userId));
        call.enqueue(new Callback<StoryLikeResponseBean>() {
            @Override
            public void onResponse(Call<StoryLikeResponseBean> call, Response<StoryLikeResponseBean> response) {
                if (response.body().getCode() == CodeUtil.Succeed) {
                    if (onGotStoryLikeListener != null)
                        onGotStoryLikeListener.onSucceed(response.body().getData());
                } else {
                    if (onGotStoryLikeListener != null)
                        onGotStoryLikeListener.onFailed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<StoryLikeResponseBean> call, Throwable t) {
                if (onGotStoryLikeListener != null)
                    onGotStoryLikeListener.onFailed(t.getMessage());

            }
        });
    }

}
