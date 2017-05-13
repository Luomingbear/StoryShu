package com.storyshu.storyshu.mvp.storyroom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.storyshu.storyshu.adapter.CommentAdapter;
import com.storyshu.storyshu.bean.getStory.StoryBean;
import com.storyshu.storyshu.info.CommentInfo;
import com.storyshu.storyshu.model.CommentModel;
import com.storyshu.storyshu.model.stories.StoryModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
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

    public StoryRoomPresenterIml(Context mContext, StoryRoomView mvpView) {
        super(mContext, mvpView);
    }

    @Override
    public void getComments() {
        /**
         * 初始化评论数据
         */
        CommentModel commentModel = new CommentModel(mContext);
        //设置评论的数量
        commentModel.getCommentsNum(new CommentModel.OnCommentSizeListener() {
            @Override
            public void onCommentSizeGot(int size) {
                mMvpView.getCommentButton().setNum(size);
            }
        });

        //获取评论的数据
        commentModel.setOnComentsGotListener(new CommentModel.OnCommentsGotListener() {
            @Override
            public void onHotCommentsGot(List<CommentInfo> commentList) {

            }

            @Override
            public void onNewCommentsGot(List<CommentInfo> commentList) {

            }

            @Override
            public void onCommentsGot(List<CommentInfo> commentList) {
                mCommentAdapter = new CommentAdapter(mContext, commentList);

                //layoutmanager
                mMvpView.getCommentRV().setLayoutManager(new LinearLayoutManager(mContext));

                //设置数据
                mMvpView.getCommentRV().setAdapter(mCommentAdapter);
            }
        });

        commentModel.getComments();
    }

    @Override
    public void showStoryPicDialog() {
        PicturePreviewDialog previewDialog = new PicturePreviewDialog(mContext);
        previewDialog.setStoryListShow(mMvpView.getStoryPic());
    }

    @Override
    public void clickLike() {

    }

    @Override
    public void clickOppose() {

    }

    @Override
    public void clickComment() {

    }

    @Override
    public void getStoryInfo() {
        if (mMvpView.getStoryIdBean() != null) {
            StoryModel storyModel = new StoryModel(mContext);
            storyModel.getStoryInfo(mMvpView.getStoryIdBean().getStoryId());
            storyModel.setOnStoryModelListener(new StoryModel.OnStoryGetListener() {
                @Override
                public void onStoriesGot(List<StoryBean> storyList) {
                    mStoryBean = storyList.get(0);
                    mMvpView.setStoryData(mStoryBean);
                }

                @Override
                public void onFailed(String error) {
                    mMvpView.showToast(error);
                }
            });

        }
    }


}
