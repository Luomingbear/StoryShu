package com.storyshu.storyshu.mvp.storyroom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.storyshu.storyshu.adapter.CommentAdapter;
import com.storyshu.storyshu.info.CommentInfo;
import com.storyshu.storyshu.model.CommentModel;
import com.storyshu.storyshu.widget.dialog.PicturePreviewDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * mvp模式
 * 故事屋的控制实现
 * Created by bear on 2017/3/30.
 */

public class StoryRoomPresenterIml implements StoryRoomPresenter {
    private StoryRoomView mStoryRoomView;
    private Context mContext;
    private CommentAdapter mCommentAdapter; //评论适配器

    public StoryRoomPresenterIml(StoryRoomView mStoryRoomView, Context mContext) {
        this.mStoryRoomView = mStoryRoomView;
        this.mContext = mContext;
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
                mStoryRoomView.getCommentButton().setNum(size);
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
                mStoryRoomView.getCommentRV().setLayoutManager(new LinearLayoutManager(mContext));

                //设置数据
                mStoryRoomView.getCommentRV().setAdapter(mCommentAdapter);
            }
        });

        commentModel.getComments();
    }

    @Override
    public void showStoryPicDialog() {
        PicturePreviewDialog previewDialog = new PicturePreviewDialog(mContext);
        List<String> list = new ArrayList<>();
        list.add("http://img.hb.aicdn.com/d64d57cb0085fd849c34bb42c913fdcbfceb780558c31-qgbz9X_fw658");
        list.add("http://img.hb.aicdn.com/72d3349abf5978236dd25f1d689a27d12c0415f3f9420-iqmLJm_fw658");
        list.add("http://img.hb.aicdn.com/04daf56af00957077cb8c2e50d41da7fc546ef7d34221-kHD0Gn_fw658");
        list.add("http://img.hb.aicdn.com/73bc52a4f32a175aab0349629333fd71179a9a392c3fa-p9wfbE_fw658");
        previewDialog.setStoryListShow(list);
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

}
