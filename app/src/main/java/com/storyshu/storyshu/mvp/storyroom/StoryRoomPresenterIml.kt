package com.storyshu.storyshu.mvp.storyroom

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils

import com.storyshu.storyshu.R
import com.storyshu.storyshu.adapter.CommentAdapter
import com.storyshu.storyshu.bean.comment.CommentBean
import com.storyshu.storyshu.bean.comment.CommentPostBean
import com.storyshu.storyshu.bean.comment.ReplyPostBean
import com.storyshu.storyshu.bean.getStory.StoryBean
import com.storyshu.storyshu.bean.getStory.StoryIdBean
import com.storyshu.storyshu.bean.like.LikePostBean
import com.storyshu.storyshu.model.CommentModel
import com.storyshu.storyshu.model.LikeModel
import com.storyshu.storyshu.model.stories.StoryModel
import com.storyshu.storyshu.model.stories.StoryPicModel
import com.storyshu.storyshu.mvp.base.IBasePresenter
import com.storyshu.storyshu.utils.KeyBordUtil
import com.storyshu.storyshu.utils.sharepreference.ISharePreference
import com.storyshu.storyshu.utils.time.TimeUtils
import com.storyshu.storyshu.widget.dialog.PicturePreviewDialog

/**
 * mvp模式
 * 故事屋的控制实现
 * Created by bear on 2017/3/30.
 */

class StoryRoomPresenterIml(mContext: Context, mvpView: StoryRoomView) :
        IBasePresenter<StoryRoomView>(mContext, mvpView), StoryRoomPresenter {
    private var mCommentAdapter: CommentAdapter? = null //评论适配器
    private var mStoryBean: StoryBean? = null //故事信息
    private var isLike = 0 //用户态度是否是喜欢 -1：不喜欢， 1：喜欢
    private var mCommentBeanList: List<CommentBean>? = null //评论列表
    private var mReplyCommentId: String? = null //回复的评论的id ：-1表示直接评论故事，其他值表示回复别人的评论

    override fun updateComments() {
        /**
         * 初始化评论数据
         */
        val commentModel = CommentModel(mContext)
        //设置评论的数量
        commentModel.getCommentsNum(mMvpView.storyId)
        commentModel.setOnCommentSizeListener(object : CommentModel.OnCommentSizeListener {
            override fun onCommentSizeGot(size: Int) {
                mMvpView.commentButton.num = size
            }

            override fun onFailed(error: String) {
                mMvpView.showToast(error)
            }
        })

        //获取评论的数据
        commentModel.getHotComments(StoryIdBean(mMvpView.storyId))
        commentModel.setOnCommentsGotListener(object : CommentModel.OnCommentsGotListener {
            override fun onHotCommentsGot(commentList: List<CommentBean>?) {
                mCommentBeanList = commentList
                mCommentAdapter = CommentAdapter(mContext, commentList)
                mCommentAdapter!!.setOnCommentClickListener(onCommentClickListener)

                //layoutmanager
                mMvpView.commentRV.layoutManager = LinearLayoutManager(mContext)

                //设置数据
                mMvpView.commentRV.adapter = mCommentAdapter

                if (commentList == null || commentList.size == 0) {
                    mMvpView.hotCommentHit.setText(R.string.no_comment)
                } else
                    mMvpView.hotCommentHit.setText(R.string.hot_comment)
            }

            override fun onNewCommentsGot(commentList: List<CommentBean>) {

            }

            override fun onCommentsGot(commentList: List<CommentBean>) {

            }

            override fun onFiled(error: String) {
                mMvpView.showToast(error)
            }
        })
    }

    /**
     * 评论点击回调接口
     */
    private val onCommentClickListener = CommentAdapter.OnCommentClickListener { position ->
        val nickname = mCommentBeanList!![position].userInfo.nickname
        mReplyCommentId = mCommentBeanList!![position].commentId
        mMvpView.commentEdit.requestFocus()
        KeyBordUtil.showKeyboard(mContext, mMvpView.commentEdit)
        mMvpView.commentEdit.hint = mContext.getString(R.string.reply_to, nickname, "")
    }

    /**
     * 隐藏键盘
     */
    fun hideKeyBoard() {
        KeyBordUtil.hideKeyboard(mContext, mMvpView.commentEdit)
    }

    override fun showStoryPicDialog(position: Int) {

        val previewDialog = PicturePreviewDialog(mContext)
        previewDialog.setStoryListShow(mMvpView.storyPic, position)
    }

    override fun clickLike() {
        if (TextUtils.isEmpty(mMvpView.storyId))
            return

        val likeModel = LikeModel(mContext)
        val likePostBean = LikePostBean(mMvpView.storyId,
                ISharePreference.getUserId(mContext), TimeUtils.getCurrentTime(), isLike != 1)
        likeModel.likeStory(likePostBean)
        likeModel.setOnLikeListener(object : LikeModel.OnLikeListener {
            override fun onSucceed() {
                if (isLike != 1) {
                    mMvpView.showToast(R.string.like)

                    //更新ui
                    mMvpView.likeButton.setClickedNoListener(true)
                    mMvpView.opposeButton.setClickedNoListener(false)

                    mMvpView.likeButton.num = mMvpView.likeButton.num + 1
                    //
                    isLike = 1
                } else {
                    mMvpView.showToast(R.string.cancel)

                    //更新ui
                    mMvpView.likeButton.setClickedNoListener(false)
                    mMvpView.likeButton.num = mMvpView.likeButton.num - 1
                    //
                    isLike -= 1
                }
                //记录

            }

            override fun onFailed(error: String) {
                mMvpView.showToast(R.string.net_error)
            }
        })
    }

    override fun clickOppose() {

    }

    override fun clickSend() {
        if (mMvpView.commentEdit.text.length == 0) {
            mMvpView.showToast(R.string.comment_issue_empty)
            return
        }
        if (!TextUtils.isEmpty(mReplyCommentId)) {
            sendReply()
            mReplyCommentId = ""
            mMvpView.commentEdit.hint = mContext.getString(R.string.edit_comment)
        } else {
            sendComment()
        }

        //隐藏键盘
        KeyBordUtil.hideKeyboard(mContext, mMvpView.commentEdit)
    }

    private val onCommentIssueListener = object : CommentModel.OnCommentIssueListener {
        override fun onSucceed() {
            mMvpView.showToast(R.string.issue_succeed)

            //更新评论
            updateComments()
            mMvpView.commentEdit.setText("")
        }

        override fun onFailed(error: String) {
//            mMvpView.showToast(error)
            mMvpView.commentEdit.setText("")
        }
    }

    /**
     * 发送评论
     */
    private fun sendComment() {
        val commentPostBean = CommentPostBean()
        commentPostBean.comment = mMvpView.commentEdit.text.toString()
        commentPostBean.createTime = TimeUtils.getCurrentTime()
        commentPostBean.storyId = mMvpView.storyId
        commentPostBean.userId = ISharePreference.getUserId(mContext)

        val commentModel = CommentModel(mContext)
        commentModel.issueComment(commentPostBean)
        commentModel.setOnCommentIssueListener(onCommentIssueListener)
    }

    /**
     * 发送回复
     */
    private fun sendReply() {
        val replyPostBean = ReplyPostBean()
        replyPostBean.comment = mMvpView.commentEdit.text.toString()
        replyPostBean.createTime = TimeUtils.getCurrentTime()
        replyPostBean.replyId = mReplyCommentId
        replyPostBean.userId = ISharePreference.getUserId(mContext)
        replyPostBean.storyId = mMvpView.storyId

        val commentModel = CommentModel(mContext)
        commentModel.replyComment(replyPostBean)
        commentModel.setOnCommentIssueListener(onCommentIssueListener)
    }

    override fun getStoryInfo() {
        if (TextUtils.isEmpty(mMvpView.storyId))
            return

        val storyModel = StoryModel(mContext)
        storyModel.getStoryInfo(mMvpView.storyId)

        storyModel.setOnStoryModelListener(object : StoryModel.OnStoryGetListener {
            override fun onStoriesGot(storyList: List<StoryBean>) {
                mStoryBean = storyList[0]
                var storybean = storyList[0]
                mMvpView.setStoryData(storybean)
                mMvpView.refreshLayout.isRefreshing = false
            }

            override fun onFailed(error: String) {
                mMvpView.showToast(error)
                mMvpView.refreshLayout.isRefreshing = false
            }
        })

    }

    override fun getStoryPic() {
        val storyPicModel = StoryPicModel()
        storyPicModel.getStoryPic(mMvpView.storyId)
        storyPicModel.setOnStoryPicGotListener(object : StoryPicModel.OnStoryPicGotListener {
            override fun onSucceed(picList: List<String>) {
                mMvpView.storyPic = picList
            }

            override fun onFailed(error: String) {
                mMvpView.showToast(error)
            }
        })
    }

    companion object {
        private val TAG = "StoryRoomPresenterIml"
    }
}
