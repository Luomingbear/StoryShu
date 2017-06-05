package com.storyshu.storyshu.mvp.storyroom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.CommentAdapter;
import com.storyshu.storyshu.bean.comment.CommentBean;
import com.storyshu.storyshu.bean.comment.CommentPostBean;
import com.storyshu.storyshu.bean.getStory.StoryBean;
import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.bean.like.LikePostBean;
import com.storyshu.storyshu.model.CommentModel;
import com.storyshu.storyshu.model.LikeModel;
import com.storyshu.storyshu.model.stories.StoryModel;
import com.storyshu.storyshu.model.stories.StoryPicModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.KeyBordUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.dialog.PicturePreviewDialog;

import java.util.List;

/**
 * mvp模式
 * 故事屋的控制实现
 * Created by bear on 2017/3/30.
 */

public class StoryRoomPresenterIml extends IBasePresenter<StoryRoomView> implements StoryRoomPresenter {
    private CommentAdapter mCommentAdapter; //评论适配器
    private StoryBean mStoryBean; //故事信息
    private int isLike = 0; //用户态度是否是喜欢 -1：不喜欢， 1：喜欢

    public StoryRoomPresenterIml(Context mContext, StoryRoomView mvpView) {
        super(mContext, mvpView);
    }

    @Override
    public void updateComments() {
        /**
         * 初始化评论数据
         */
        CommentModel commentModel = new CommentModel(mContext);
        //设置评论的数量
        commentModel.getCommentsNum(mMvpView.getStoryId());
        commentModel.setOnCommentSizeListener(new CommentModel.OnCommentSizeListener() {
            @Override
            public void onCommentSizeGot(int size) {
                mMvpView.getCommentButton().setNum(size);
            }

            @Override
            public void onFailed(String error) {
                mMvpView.showToast(error);
            }
        });

        //获取评论的数据
        commentModel.getHotComments(new StoryIdBean(mMvpView.getStoryId()));
        commentModel.setOnCommentsGotListener(new CommentModel.OnCommentsGotListener() {
            @Override
            public void onHotCommentsGot(List<CommentBean> commentList) {
                mCommentAdapter = new CommentAdapter(mContext, commentList);

                //layoutmanager
                mMvpView.getCommentRV().setLayoutManager(new LinearLayoutManager(mContext));

                //设置数据
                mMvpView.getCommentRV().setAdapter(mCommentAdapter);

                if (commentList == null || commentList.size() == 0) {
                    mMvpView.getHotCommentHit().setVisibility(View.GONE);
                } else
                    mMvpView.getHotCommentHit().setVisibility(View.VISIBLE);
            }

            @Override
            public void onNewCommentsGot(List<CommentBean> commentList) {

            }

            @Override
            public void onCommentsGot(List<CommentBean> commentList) {

            }

            @Override
            public void onFiled(String error) {
                mMvpView.showToast(error);
            }
        });
    }

    @Override
    public void showStoryPicDialog() {

        PicturePreviewDialog previewDialog = new PicturePreviewDialog(mContext);
        previewDialog.setStoryListShow(mMvpView.getStoryPic());
    }

    @Override
    public void clickLike() {
        if (TextUtils.isEmpty(mMvpView.getStoryId()))
            return;

        LikeModel likeModel = new LikeModel(mContext);
        LikePostBean likePostBean = new LikePostBean(mMvpView.getStoryId(),
                ISharePreference.getUserId(mContext), TimeUtils.getCurrentTime(), isLike != 1);
        likeModel.likeStory(likePostBean);
        likeModel.setOnLikeListener(new LikeModel.OnLikeListener() {
            @Override
            public void onSucceed() {
                if (isLike != 1) {
                    mMvpView.showToast(R.string.like);

                    //更新ui
                    mMvpView.getLikeButton().setClickedNoListener(true);
                    mMvpView.getOpposeButton().setClickedNoListener(false);

                    mMvpView.getLikeButton().setNum(mMvpView.getLikeButton().getNum() + 1);
                    //
                    isLike = 1;
                } else {
                    mMvpView.showToast(R.string.cancel);

                    //更新ui
                    mMvpView.getLikeButton().setClickedNoListener(false);
                    mMvpView.getLikeButton().setNum(mMvpView.getLikeButton().getNum() - 1);
                    //
                    isLike -= 1;
                }
                //记录

            }

            @Override
            public void onFailed(String error) {
                mMvpView.showToast(R.string.net_error);
            }
        });
    }

    @Override
    public void clickOppose() {

    }

    @Override
    public void clickSend() {
        if (mMvpView.getCommentEdit().getText().length() == 0) {
            mMvpView.showToast(R.string.comment_issue_empty);
            return;
        }

        CommentPostBean commentPostBean = new CommentPostBean();
        commentPostBean.setComment(mMvpView.getCommentEdit().getText().toString());
        commentPostBean.setCreateTime(TimeUtils.getCurrentTime());
        commentPostBean.setStoryId(mMvpView.getStoryId());
        commentPostBean.setUserId(ISharePreference.getUserId(mContext));

        CommentModel commentModel = new CommentModel(mContext);
        commentModel.issueComment(commentPostBean);
        commentModel.setOnCommentIssueListener(new CommentModel.OnCommentIssueListener() {
            @Override
            public void onSucceed() {
                mMvpView.showToast(R.string.issue_succeed);

                //更新评论
                updateComments();
                mMvpView.getCommentEdit().setText("");
            }

            @Override
            public void onFailed(String error) {
                mMvpView.showToast(error);
                mMvpView.getCommentEdit().setText("");
            }
        });

        //隐藏键盘
        KeyBordUtil.hideKeyboard(mContext, mMvpView.getCommentEdit());
    }

    @Override
    public void getStoryInfo() {
        if (TextUtils.isEmpty(mMvpView.getStoryId()))
            return;

        StoryModel storyModel = new StoryModel(mContext);
        storyModel.getStoryInfo(mMvpView.getStoryId());

        storyModel.setOnStoryModelListener(new StoryModel.OnStoryGetListener() {
            @Override
            public void onStoriesGot(List<StoryBean> storyList) {
                mStoryBean = storyList.get(0);
                mMvpView.setStoryData(mStoryBean);
                mMvpView.getRefreshLayout().setRefreshing(false);
            }

            @Override
            public void onFailed(String error) {
                mMvpView.showToast(error);
                mMvpView.getRefreshLayout().setRefreshing(false);
            }
        });

    }

    @Override
    public void getStoryPic() {
        StoryPicModel storyPicModel = new StoryPicModel();
        storyPicModel.getStoryPic(mMvpView.getStoryId());
        storyPicModel.setOnStoryPicGotListener(new StoryPicModel.OnStoryPicGotListener() {
            @Override
            public void onSucceed(List<String> picList) {
                mMvpView.setStoryPic(picList);
            }

            @Override
            public void onFailed(String error) {
                mMvpView.showToast(error);
            }
        });
    }
}
