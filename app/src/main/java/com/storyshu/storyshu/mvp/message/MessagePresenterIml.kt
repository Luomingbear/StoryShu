package com.storyshu.storyshu.mvp.message

import android.content.Context
import android.view.View
import android.widget.ExpandableListView

import com.storyshu.storyshu.adapter.MessageExpandableAdapter
import com.storyshu.storyshu.adapter.SystemMessageAdapter
import com.storyshu.storyshu.bean.getStory.StoryIdBean
import com.storyshu.storyshu.bean.read.ReadCommentPostBean
import com.storyshu.storyshu.bean.read.ReadStoryLikeBean
import com.storyshu.storyshu.bean.read.ReadStoryLikePostBean
import com.storyshu.storyshu.info.StoryMessageInfo
import com.storyshu.storyshu.info.SystemMessageInfo
import com.storyshu.storyshu.model.MessageModel
import com.storyshu.storyshu.mvp.base.IBasePresenter

import java.util.ArrayList

/**
 * mvp模式
 * 消息页面的代理实现
 * Created by bear on 2017/3/21.
 */

class MessagePresenterIml(mContext: Context, mvpView: MessageView) : IBasePresenter<MessageView>(mContext, mvpView), MessagePresenter {
    private var mMessageModel: MessageModel? = null //信息更新的model
    private var mLikeList: ArrayList<StoryMessageInfo>? = null //喜欢我的列表
    private var mCommentList: ArrayList<StoryMessageInfo>? = null //评论我的列表
    private var mSystemMessageList: ArrayList<SystemMessageInfo>? = null //系统消息的列表
    private var mLikeExpandableAdapter: MessageExpandableAdapter? = null //点赞显示适配器
    private var mCommentExpandableAdapter: MessageExpandableAdapter? = null //评论显示适配器
    private var mSystemExpandableAdapter: SystemMessageAdapter? = null //系统信息显示适配器

    init {
        init()
    }

    private fun init() {
        mLikeList = ArrayList<StoryMessageInfo>()
        mCommentList = ArrayList<StoryMessageInfo>()
        mSystemMessageList = ArrayList<SystemMessageInfo>()

        mLikeExpandableAdapter = MessageExpandableAdapter(mContext, mLikeList)
        mMvpView.likeMessageList.setAdapter(mLikeExpandableAdapter)
        mMvpView.likeMessageList.visibility = View.GONE

        mCommentExpandableAdapter = MessageExpandableAdapter(mContext, mCommentList)
        mMvpView.commentMessageList.setAdapter(mCommentExpandableAdapter)
        mMvpView.commentMessageList.visibility = View.GONE

        mSystemExpandableAdapter = SystemMessageAdapter(mContext, mSystemMessageList)
        mMvpView.systemMessageList.setAdapter(mSystemExpandableAdapter)
        mMvpView.systemMessageList.visibility = View.GONE

        //点击选项
        setClickEvents()
    }

    private val messageModelListener = object : MessageModel.OnMessageModelListener {
        override fun onLikeDataGot(messageInfoList: ArrayList<StoryMessageInfo>?) {

            mLikeList!!.clear()
            if (messageInfoList != null && messageInfoList.size > 0) {
                for (storyMessageInfo in messageInfoList) {
                    mLikeList!!.add(storyMessageInfo)
                }
            }

            if (mLikeList!!.size == 0)
                mMvpView.likeMessageList.visibility = View.GONE
            else {
                mMvpView.likeMessageList.visibility = View.VISIBLE
                mLikeExpandableAdapter!!.notifyDataSetChanged()
            }


        }

        override fun onCommentDataGot(messageList: ArrayList<StoryMessageInfo>?) {

            mCommentList!!.clear()
            if (messageList != null && messageList.size > 0) {
                for (storyMessageInfo in messageList) {
                    mCommentList!!.add(storyMessageInfo)
                }
            }

            if (mCommentList!!.size == 0)
                mMvpView.commentMessageList.visibility = View.GONE
            else {
                mMvpView.commentMessageList.visibility = View.VISIBLE
                mCommentExpandableAdapter!!.notifyDataSetChanged()
            }

        }

        override fun onSystemDataGot(messageList: ArrayList<SystemMessageInfo>) {
            mSystemMessageList = messageList

            if (mSystemMessageList!!.size == 0)
                mMvpView.systemMessageList.visibility = View.GONE
            else {
                mMvpView.systemMessageList.visibility = View.VISIBLE
                //显示
                showMessageList()
            }

            mMvpView.refreshLayout.isRefreshing = false
        }
    }

    /**
     * 获取消息数据
     */
    override fun getMessageData() {
        //获取消息列表
        mMessageModel = MessageModel(mContext)
        mMessageModel!!.updateMessageData(messageModelListener)
    }

    /**
     * 标记我收到的赞为已读
     */
    private fun readStoryLike() {
        val messageModel = MessageModel(mContext)
        val readStoryLikeBeanList = ArrayList<ReadStoryLikeBean>()
        for (storyMessageInfo in mLikeList!!) {
            readStoryLikeBeanList.add(ReadStoryLikeBean(storyMessageInfo.userInfo.userId,
                    storyMessageInfo.storyId))
        }
        messageModel.updateStoryLikeRead(ReadStoryLikePostBean(readStoryLikeBeanList))
    }

    /**
     * 标记我收到的评论为已读
     */
    private fun readComment() {
        val messageModel = MessageModel(mContext)
        val list = ArrayList<String>()
        for (storyMessageInfo in mCommentList!!) {
            list.add(storyMessageInfo.commentId)
        }
        messageModel.updateStoryCommentRead(ReadCommentPostBean(list))
    }

    /**
     * 初始化点击事件
     */
    private fun setClickEvents() {
        //点赞列表
        mMvpView.likeMessageList.setOnGroupClickListener(ExpandableListView.OnGroupClickListener { parent, v, groupPosition, id ->
            if (mLikeList!!.size == 0)
                return@OnGroupClickListener false

            mLikeList!![0].unReadNum = 0
            mLikeExpandableAdapter!!.notifyDataSetChanged()
            //
            readStoryLike()
            false
        })
        mMvpView.likeMessageList.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            mMvpView.intent2StoryRoom(StoryIdBean(mLikeList!![childPosition].storyId))
            false
        }

        //评论列表
        mMvpView.commentMessageList.setOnGroupClickListener(ExpandableListView.OnGroupClickListener { parent, v, groupPosition, id ->
            if (mCommentList!!.size == 0)
                return@OnGroupClickListener false

            mCommentList!![0].unReadNum = 0
            mCommentExpandableAdapter!!.notifyDataSetChanged()
            //
            readComment()
            false
        })
        mMvpView.commentMessageList.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            mMvpView.intent2StoryRoom(StoryIdBean(mCommentList!![childPosition].storyId))
            false
        }

        //系统信息列表
        mMvpView.systemMessageList.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            //                    mMvpView.intent2StoryRoom(new StoryIdBean(mSystemMessageList.get(childPosition).getStoryId()));
            false
        }
    }

    override fun showMessageList() {

    }

    override fun unFoldList(listType: MessagePresenter.ListType) {

    }

    override fun FoldList(listType: MessagePresenter.ListType) {

    }

    override fun toStoryRoom() {

    }

    override fun toComputerMessage() {

    }

    companion object {
        private val TAG = "MessagePresenterIml"
    }
}
