package com.storyshu.storyshu.mvp.message_me

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.storyshu.storyshu.R
import com.storyshu.storyshu.adapter.MessageMeAdapter
import com.storyshu.storyshu.bean.getStory.StoryIdBean
import com.storyshu.storyshu.bean.read.ReadCommentPostBean
import com.storyshu.storyshu.bean.read.ReadStoryLikePostBean
import com.storyshu.storyshu.info.MessageInfo
import com.storyshu.storyshu.info.StoryMessageInfo
import com.storyshu.storyshu.info.SystemMessageInfo
import com.storyshu.storyshu.model.MessageModel
import com.storyshu.storyshu.mvp.base.IBasePresenter

/**
 * mvp
 * 未读消息列表 逻辑实现
 * Created by bear on 2017/9/16.
 */
class MessageMePresenter(context: Context, view: MessageMeView) :
        IBasePresenter<MessageMeView>(context, view) {

    private var mList: MutableList<StoryMessageInfo>
    private var mAdapter: MessageMeAdapter

    private val model = MessageModel(mContext)

    init {
        mList = ArrayList<StoryMessageInfo>()
        mAdapter = MessageMeAdapter(mContext, mList)
    }

    fun initRefreshLayout() {
        mMvpView.refreshLayout.setColorSchemeResources(R.color.colorRed)
        mMvpView.refreshLayout.setOnRefreshListener {
            mList.clear()
            mAdapter.notifyDataSetChanged()

            getRvData()
        }
    }

    fun initRv() {
        mAdapter.setOnItemClickListener { adapter, view, position ->
            mMvpView.intentStoryRoom(StoryIdBean(mList[position].storyId))
        }

        mMvpView.messageRv.adapter = mAdapter
        mMvpView.messageRv.layoutManager = LinearLayoutManager(mContext)

    }

    /**
     * 获取数据
     */
    fun getRvData() {
        when (mMvpView.getMessageType()) {
            MessageInfo.LIKE -> {
                model.getLikeList()
            }
            MessageInfo.COMMENT -> {
                model.getCommentList()
            }
        }

        model.setOnMessageModelListener(object : MessageModel.OnMessageModelListener {
            override fun onLikeDataGot(messageList: java.util.ArrayList<StoryMessageInfo>?) {
                if (messageList != null && messageList?.size ?: 0 > 0) {
                    mList.addAll(messageList)
                    mAdapter.notifyDataSetChanged()
                }

                mMvpView.refreshLayout.isRefreshing = false
            }

            override fun onCommentDataGot(messageList: java.util.ArrayList<StoryMessageInfo>?) {
                if (messageList != null && messageList?.size ?: 0 > 0) {
                    mList.addAll(messageList)
                    mAdapter.notifyDataSetChanged()
                }

                mMvpView.refreshLayout.isRefreshing = false
            }

            override fun onSystemDataGot(messageList: java.util.ArrayList<SystemMessageInfo>?) {
//                if (messageList != null && messageList?.size ?: 0 > 0) {
//                    mList.addAll(messageList)
//                    mAdapter.notifyDataSetChanged()
//                }

                mMvpView.refreshLayout.isRefreshing = false
            }
        })
    }

    /**
     * 标记为已读
     */
    fun makeRead() {
        when (mMvpView.getMessageType()) {
            MessageInfo.LIKE -> {
                readLike()
            }
            MessageInfo.COMMENT -> {
                readComment()
            }
        }
    }

    /**
     * 将点赞列表标记为已读
     */
    fun readLike() {
        if (mList.size == 0)
            return

        val likeList = ArrayList<String>()
        for (info in mList) {
            likeList.add(info.id)
        }
        model.updateStoryLikeRead(ReadStoryLikePostBean(likeList))
    }

    /**
     * 将评论标记为已读
     */
    fun readComment() {
        if (mList.size == 0)
            return

        val likeList = ArrayList<String>()
        for (info in mList) {
            likeList.add(info.id)
        }
        model.updateStoryCommentRead(ReadCommentPostBean(likeList))
    }
}