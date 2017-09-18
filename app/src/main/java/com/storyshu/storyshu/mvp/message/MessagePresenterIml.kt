package com.storyshu.storyshu.mvp.message

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.storyshu.storyshu.R
import com.storyshu.storyshu.adapter.MessageListAdapter
import com.storyshu.storyshu.bean.getStory.StoryIdBean
import com.storyshu.storyshu.info.MessageInfo
import com.storyshu.storyshu.info.StoryMessageInfo
import com.storyshu.storyshu.info.SystemMessageInfo
import com.storyshu.storyshu.model.MessageModel
import com.storyshu.storyshu.mvp.base.IBasePresenter
import com.storyshu.storyshu.utils.ToastUtil
import com.storyshu.storyshu.utils.sharepreference.ISharePreference


/**
 * mvp模式
 * 消息页面的代理实现
 * Created by bear on 2017/3/21.
 */

class MessagePresenterIml(mContext: Context, mvpView: MessageView) : IBasePresenter<MessageView>(mContext, mvpView), MessagePresenter {
    private val TAG = "MessagePresenterIml"

    private var mMessageModel: MessageModel? = null //信息更新的model

    private var mList: MutableList<MessageInfo>
    private var mAdapter: MessageListAdapter

    init {

        mList = ArrayList<MessageInfo>()
        mAdapter = MessageListAdapter(mContext, mList)
        mMvpView.messageRecyclerView.adapter = mAdapter
        mvpView.messageRecyclerView.layoutManager = LinearLayoutManager(mContext)

        mAdapter.setOnItemClickListener { adapter, view, position ->
            when (mList[position].type) {
                MessageInfo.COMMENT,
                MessageInfo.LIKE -> {
                    mMvpView.intent2MessageMe(mList[position].type)
                }

                MessageInfo.DISCUSS,
                MessageInfo.CHAT -> {
                    mMvpView.intent2DiscussRoom(StoryIdBean(mList[position].storyId))

                }
            }
        }

    }

    fun initRefreshLayout() {
        mMvpView.refreshLayout.setColorSchemeResources(R.color.colorRed)
        mMvpView.refreshLayout.setOnRefreshListener {
            mList.clear()
            mAdapter.notifyDataSetChanged()
            getMessageData()
        }
    }

    /**
     * 获取消息数据
     */
    override fun getMessageData() {
        //获取消息列表
        mMessageModel = MessageModel(mContext)
        mMessageModel?.getDiscussList(ISharePreference.getUserId(mContext), object : MessageModel.MessageGotListener {
            override fun onSucceed(list: MutableList<MessageInfo>) {
                mList.addAll(list)
                mAdapter.notifyDataSetChanged()
                mMvpView.refreshLayout.isRefreshing = false
            }

            override fun onFailed(error: String?) {
                ToastUtil.Show(mContext, error)
                mMvpView.refreshLayout.isRefreshing = false
            }
        })

        //获取点赞等数据
        getUnreadNum()

    }

    /**
     * 获取与自己有关的点赞和评论数量
     */
    private fun getUnreadNum() {
        mMessageModel?.updateMessageData(object : MessageModel.OnMessageModelListener {
            override fun onLikeDataGot(messageList: java.util.ArrayList<StoryMessageInfo>?) {
                if (messageList?.size == 0)
                    return

                val info = MessageInfo()
                info.content = "" + messageList?.size
                info.type = MessageInfo.LIKE

                mList.add(info)

                mMvpView.notifyDataSetChanged()
            }

            override fun onCommentDataGot(messageList: java.util.ArrayList<StoryMessageInfo>?) {
                if (messageList?.size == 0)
                    return
                val info = MessageInfo()
                info.content = "" + messageList?.size
                info.type = MessageInfo.COMMENT
                mList.add(info)

                mMvpView.notifyDataSetChanged()
            }

            override fun onSystemDataGot(messageList: java.util.ArrayList<SystemMessageInfo>?) {
                if (messageList?.size == 0)
                    return
            }
        })

    }

    private fun getUnreadCommentNum() {

    }

    val msgListener = object : EMMessageListener {

        override fun onMessageReceived(messages: List<EMMessage>) {
            for (message in messages) {
                for (roomMsg in mList) {
                    if (roomMsg.roomId.equals(message.to)) {
                        roomMsg.userId = message.from.toInt()
                        roomMsg.content = message.body.toString()
                        mMvpView.notifyDataSetChanged()
                    }
                }

            }

            EMClient.getInstance().chatManager().importMessages(messages);
            Log.d(TAG, messages[0].toString())
            //收到消息
        }

        override fun onCmdMessageReceived(messages: List<EMMessage>) {
            //收到透传消息
        }

        override fun onMessageRead(messages: List<EMMessage>) {
            //收到已读回执
        }

        override fun onMessageDelivered(message: List<EMMessage>) {
            //收到已送达回执
        }

        override fun onMessageRecalled(messages: List<EMMessage>) {
            //消息被撤回
        }

        override fun onMessageChanged(message: EMMessage, change: Any) {
            //消息状态变动
        }
    }

    /**
     * 添加环信消息监听
     */
    fun addEMMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(msgListener)
    }

    /**
     * 移除环信消息监听
     */
    fun removeEMMessageListener() {
        EMClient.getInstance().chatManager().removeMessageListener(msgListener)
    }

    override fun toStoryRoom() {

    }

    override fun toComputerMessage() {

    }

}
