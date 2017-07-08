package com.storyshu.storyshu.model;

import android.content.Context;
import android.util.Log;

import com.storyshu.storyshu.bean.OnlyDataResponseBean;
import com.storyshu.storyshu.bean.message.StoryCommentBean;
import com.storyshu.storyshu.bean.message.StoryCommentResponseBean;
import com.storyshu.storyshu.bean.message.StoryLikeBean;
import com.storyshu.storyshu.bean.read.ReadCommentPostBean;
import com.storyshu.storyshu.bean.read.ReadStoryLikePostBean;
import com.storyshu.storyshu.bean.user.UserIdBean;
import com.storyshu.storyshu.info.StoryMessageInfo;
import com.storyshu.storyshu.info.SystemMessageInfo;
import com.storyshu.storyshu.utils.net.CodeUtil;
import com.storyshu.storyshu.utils.net.RetrofitManager;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * "与我有关的信息"的数据操作
 * Created by bear on 2017/3/21.
 */

public class MessageModel {
    private static final String TAG = "MessageModel";
    private Context mContext;
    private OnMessageModelListener onMessageModelListener;

    public void setOnMessageModelListener(OnMessageModelListener onMessageModelListener) {
        this.onMessageModelListener = onMessageModelListener;
    }

    /**
     * 获取与我有关信息的回调函数
     */
    public interface OnMessageModelListener {
        void onLikeDataGot(ArrayList<StoryMessageInfo> messageList);

        void onCommentDataGot(ArrayList<StoryMessageInfo> messageList);

        void onSystemDataGot(ArrayList<SystemMessageInfo> messageList);
    }

    public MessageModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取点赞的列表数据
     */
    private void getLikeList() {
        LikeModel likeModel = new LikeModel(mContext);
        likeModel.getStoryLike(ISharePreference.getUserId(mContext));
        likeModel.setOnGotStoryLikeListener(new LikeModel.OnGotStoryLikeListener() {
            @Override
            public void onSucceed(List<StoryLikeBean> storyLikeBeanList) {
                final ArrayList<StoryMessageInfo> likeList = new ArrayList<>();
                if (storyLikeBeanList != null && storyLikeBeanList.size() > 0) {
                    for (StoryLikeBean storyLikeBean : storyLikeBeanList)
                        likeList.add(new StoryMessageInfo(storyLikeBean.getUserInfo(), storyLikeBean.getCreateTime(),
                                storyLikeBean.getStoryId(), "", storyLikeBean.getContent(), storyLikeBean.getCover(), "",
                                StoryMessageInfo.MessageType.LIKE_STORY, storyLikeBeanList.size()));
                }
                if (onMessageModelListener != null)
                    onMessageModelListener.onLikeDataGot(likeList);
            }

            @Override
            public void onFailed(String error) {

            }
        });

    }

    /**
     * 获取评论数据
     */
    private void getCommentList() {
        Call<StoryCommentResponseBean> call = RetrofitManager.getInstance().getService().getStoryComment(new UserIdBean(ISharePreference.getUserId(mContext)));
        call.enqueue(new Callback<StoryCommentResponseBean>() {
            @Override
            public void onResponse(Call<StoryCommentResponseBean> call, Response<StoryCommentResponseBean> response) {
                Log.i(TAG, "onResponse: " + response.body());
                if (response.body().getCode() == CodeUtil.Succeed) {
                    ArrayList<StoryMessageInfo> list = new ArrayList<>();
                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        for (StoryCommentBean storyCommentBean : response.body().getData()) {
                            list.add(new StoryMessageInfo(storyCommentBean.getUserInfo(), storyCommentBean.getCreateTime(),
                                    storyCommentBean.getStoryId(), storyCommentBean.getCommentId(), storyCommentBean.getContent(),
                                    storyCommentBean.getCover(), storyCommentBean.getComment(), StoryMessageInfo.MessageType.COMMENT,
                                    response.body().getData().size()));
                        }
                        if (onMessageModelListener != null)
                            onMessageModelListener.onCommentDataGot(list);
                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<StoryCommentResponseBean> call, Throwable t) {

            }
        });
    }

    /**
     * 获取系统消息列表
     */
    private void getSystemList() {
        // TODO: 2017/5/12 获取系统信息
        ArrayList<SystemMessageInfo> systemList = new ArrayList<>();

        if (onMessageModelListener != null)
            onMessageModelListener.onSystemDataGot(systemList);
    }

    /**
     * 更新"我的信息"
     */
    public void updateMessageData(OnMessageModelListener onMessageModelListener) {
        this.onMessageModelListener = onMessageModelListener;
        getLikeList();
        getCommentList();
        getSystemList();
    }

    /**
     * 标记我收到的赞为已读
     *
     * @param readStoryLikePostBean
     */
    public void updateStoryLikeRead(ReadStoryLikePostBean readStoryLikePostBean) {
        Call<OnlyDataResponseBean> call = RetrofitManager.getInstance().getService().updateStoryLikeRead(readStoryLikePostBean);
        call.enqueue(new Callback<OnlyDataResponseBean>() {
            @Override
            public void onResponse(Call<OnlyDataResponseBean> call, Response<OnlyDataResponseBean> response) {
                if (response.body().getCode() == CodeUtil.Succeed) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<OnlyDataResponseBean> call, Throwable t) {

            }
        });
    }

    /**
     * 更新我收到的评论为已读
     *
     * @param readCommentPostBean
     */
    public void updateStoryCommentRead(ReadCommentPostBean readCommentPostBean) {
        Call<OnlyDataResponseBean> call = RetrofitManager.getInstance().getService().updateStoryCommentRead(readCommentPostBean);
        call.enqueue(new Callback<OnlyDataResponseBean>() {
            @Override
            public void onResponse(Call<OnlyDataResponseBean> call, Response<OnlyDataResponseBean> response) {
                if (response.body().getCode() == CodeUtil.Succeed) {

                } else {
                }
            }

            @Override
            public void onFailure(Call<OnlyDataResponseBean> call, Throwable t) {

            }
        });
    }

    public void getUnreadNum(int userID, final OnOnlyDataResponseListener listener) {
        if (listener == null)
            return;

        Call<OnlyDataResponseBean> call = RetrofitManager.getInstance().getService().getUnreadNum(new UserIdBean(userID));
        call.enqueue(new Callback<OnlyDataResponseBean>() {
            @Override
            public void onResponse(Call<OnlyDataResponseBean> call, Response<OnlyDataResponseBean> response) {
                if (response.body().getCode() == CodeUtil.Succeed) {
                    listener.onSucceed(Integer.parseInt(response.body().getData()));
                } else {
                    listener.onFalied(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<OnlyDataResponseBean> call, Throwable t) {
                listener.onFalied(t.getMessage());
            }
        });
    }
}
