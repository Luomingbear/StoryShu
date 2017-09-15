package com.storyshu.storyshu.mvp.message_me

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.storyshu.storyshu.R
import com.storyshu.storyshu.adapter.MessageMeAdapter
import com.storyshu.storyshu.bean.getStory.StoryIdBean
import com.storyshu.storyshu.info.MessageInfo
import com.storyshu.storyshu.info.StoryMessageInfo
import com.storyshu.storyshu.info.SystemMessageInfo
import com.storyshu.storyshu.model.MessageModel
import com.storyshu.storyshu.mvp.base.IBasePresenter

/**
 * Created by bear on 2017/9/16.
 */
class MessageMePresenter(context: Context, view: MessageMeView) :
        IBasePresenter<MessageMeView>(context, view) {

    private var mList: MutableList<StoryMessageInfo>
    private var mAdapter: MessageMeAdapter

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

    fun getRvData() {
        val model = MessageModel(mContext)
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
}