package com.storyshu.storyshu.model;

import android.content.Context;

import com.storyshu.storyshu.bean.OnlyDataResponseBean;
import com.storyshu.storyshu.bean.comment.CommentBean;
import com.storyshu.storyshu.bean.comment.CommentPostBean;
import com.storyshu.storyshu.bean.comment.CommentReponseBean;
import com.storyshu.storyshu.bean.comment.CommentSizeResponseBean;
import com.storyshu.storyshu.bean.comment.ReplyPostBean;
import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.utils.net.CodeUtil;
import com.storyshu.storyshu.utils.net.RetrofitManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 评论的数据管理
 * Created by bear on 2017/3/30.
 */

public class CommentModel {
    private Context mContext;
    private OnCommentsGotListener onCommentsGotListener;
    private OnCommentIssueListener onCommentIssueListener;
    private OnCommentSizeListener onCommentSizeListener;

    public void setOnCommentsGotListener(OnCommentsGotListener onCommentsGotListener) {
        this.onCommentsGotListener = onCommentsGotListener;
    }

    public void setOnCommentIssueListener(OnCommentIssueListener onCommentIssueListener) {
        this.onCommentIssueListener = onCommentIssueListener;
    }

    public void setOnCommentSizeListener(OnCommentSizeListener onCommentSizeListener) {
        this.onCommentSizeListener = onCommentSizeListener;
    }

    public interface OnCommentsGotListener {
        /**
         * 热门的评论获取成功
         *
         * @param commentList
         */
        void onHotCommentsGot(List<CommentBean> commentList);

        /**
         * 获取的最新的评论
         *
         * @param commentList
         */
        void onNewCommentsGot(List<CommentBean> commentList);

        /**
         * 评论获取成功的回调
         *
         * @param commentList
         */
        void onCommentsGot(List<CommentBean> commentList);

        void onFiled(String error);
    }

    /**
     * 获取评论总数的接口
     */
    public interface OnCommentSizeListener {
        void onCommentSizeGot(int size);

        void onFailed(String error);
    }

    /**
     * 发表评论的回调
     */
    public interface OnCommentIssueListener {
        void onSucceed();

        void onFailed(String error);
    }

    public CommentModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取热门的评论
     */
    public void getHotComments(StoryIdBean storyIdBean) {
        Call<CommentReponseBean> call = RetrofitManager.getInstance().getService().getHotComment(storyIdBean);
        call.enqueue(new Callback<CommentReponseBean>() {
            @Override
            public void onResponse(Call<CommentReponseBean> call, Response<CommentReponseBean> response) {
                if (response.body().getCode() == CodeUtil.Succeed) {
                    for (CommentBean commentBean : response.body().getData()) {
                        commentBean.setTag("HOT");
                    }

                    if (onCommentsGotListener != null)
                        onCommentsGotListener.onHotCommentsGot(response.body().getData());
                } else {
                    if (onCommentsGotListener != null)
                        onCommentsGotListener.onFiled(response.body().getMessage());

                }
            }

            @Override
            public void onFailure(Call<CommentReponseBean> call, Throwable t) {

                if (onCommentsGotListener != null)
                    onCommentsGotListener.onFiled(t.getMessage());
            }
        });
    }


    /**
     * 获取最新的评论
     */
    public void getNestComment(StoryIdBean storyIdBean) {
        Call<CommentReponseBean> call = RetrofitManager.getInstance().getService().getNestComment(storyIdBean);
        call.enqueue(new Callback<CommentReponseBean>() {
            @Override
            public void onResponse(Call<CommentReponseBean> call, Response<CommentReponseBean> response) {
                if (response.body().getCode() == CodeUtil.Succeed) {
                    if (onCommentsGotListener != null)
                        onCommentsGotListener.onNewCommentsGot(response.body().getData());

                } else {
                    if (onCommentsGotListener != null)
                        onCommentsGotListener.onFiled(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommentReponseBean> call, Throwable t) {
                if (onCommentsGotListener != null)
                    onCommentsGotListener.onFiled(t.getMessage());
            }
        });

    }

    /**
     * 获取该故事目前一共的评论数量
     */
    public void getCommentsNum(String storyId) {
        Call<CommentSizeResponseBean> call = RetrofitManager.getInstance().getService().getCommentSize(new StoryIdBean(storyId));
        call.enqueue(new Callback<CommentSizeResponseBean>() {
            @Override
            public void onResponse(Call<CommentSizeResponseBean> call, Response<CommentSizeResponseBean> response) {
                if (response.body().getCode() == CodeUtil.Succeed) {
                    if (onCommentSizeListener != null) {
                        onCommentSizeListener.onCommentSizeGot(response.body().getData());
                    }

                } else {
                    if (onCommentSizeListener != null)
                        onCommentSizeListener.onFailed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommentSizeResponseBean> call, Throwable t) {
                if (onCommentSizeListener != null)
                    onCommentSizeListener.onFailed(t.getMessage());
            }
        });

    }

    /**
     * 发表评论
     *
     * @param commentPostBean
     */
    public void issueComment(CommentPostBean commentPostBean) {
        Call<OnlyDataResponseBean> call = RetrofitManager.getInstance().getService().issueComment(commentPostBean);
        call.enqueue(new Callback<OnlyDataResponseBean>() {
            @Override
            public void onResponse(Call<OnlyDataResponseBean> call, Response<OnlyDataResponseBean> response) {

//                if (response.body().getCode() == CodeUtil.Succeed) {
//                    if (onCommentIssueListener != null)
//                        onCommentIssueListener.onSucceed();
//                } else {
//                    if (onCommentIssueListener != null)
//                        onCommentIssueListener.onFailed(response.body().getMessage());
//
//                }
            }

            @Override
            public void onFailure(Call<OnlyDataResponseBean> call, Throwable t) {
                if (onCommentIssueListener != null)
                    onCommentIssueListener.onFailed(t.getMessage());

            }
        });
    }

    /**
     * 回复评论
     */
    public void replyComment(ReplyPostBean replyPostBean) {
        Call<OnlyDataResponseBean> call = RetrofitManager.getInstance().getService().replyComment(replyPostBean);
        call.enqueue(new Callback<OnlyDataResponseBean>() {
            @Override
            public void onResponse(Call<OnlyDataResponseBean> call, Response<OnlyDataResponseBean> response) {
                if (response.body().getCode() == CodeUtil.Succeed) {
                    if (onCommentIssueListener != null)
                        onCommentIssueListener.onSucceed();
                } else {
                    if (onCommentIssueListener != null)
                        onCommentIssueListener.onFailed(response.body().getMessage());

                }

            }

            @Override
            public void onFailure(Call<OnlyDataResponseBean> call, Throwable t) {
                if (onCommentIssueListener != null)
                    onCommentIssueListener.onFailed(t.getMessage());
            }
        });

    }
}
