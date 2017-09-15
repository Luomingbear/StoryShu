package com.storyshu.storyshu.adapter

import android.content.Context
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.hyphenate.chat.EMMessage
import com.storyshu.storyshu.R
import com.storyshu.storyshu.info.BaseUserInfo
import com.storyshu.storyshu.model.UserModel
import com.storyshu.storyshu.utils.sharepreference.ISharePreference
import com.storyshu.storyshu.utils.time.TimeUtils
import com.storyshu.storyshu.widget.imageview.ChatAvatarView

/**
 * 讨论的Rv适配器
 * Created by bear on 2017/5/25.
 */

class DiscussAdapter(mContext: Context, var mDiscussList: List<EMMessage> //聊天的信息数据列表
) : BaseQuickAdapter<EMMessage, BaseViewHolder>(mDiscussList) {
    private var mUserId = 0 //当前用户的id

    init {
        mUserId = ISharePreference.getUserId(mContext)

        multiTypeDelegate = object : MultiTypeDelegate<EMMessage>() {
            override fun getItemType(t: EMMessage?): Int {
                if ("" + mUserId == t?.from ?: "")
                    return ME
                else
                    return OTHER
            }
        }

        multiTypeDelegate.registerItemType(ME, R.layout.discuss_me_layout)
        multiTypeDelegate.registerItemType(OTHER, R.layout.discuss_other_layout)


    }


    override fun convert(helper: BaseViewHolder, item: EMMessage) {

        helper?.getView<ChatAvatarView>(R.id.avatar)?.setUserId(Integer.parseInt(item?.from), object : UserModel.OnUserInfoGetListener {
            override fun onSucceed(userInfo: BaseUserInfo) {
                helper?.setText(R.id.nickname, userInfo?.nickname)

            }

            override fun onFailed(error: String) {

            }
        })

        //内容
        val content = item.body?.toString()?.substring(3, item.body.toString().length - 1)
        helper?.setText(R.id.content, content)


        /**
         * 信息距离上一条的时间小于一分钟，则不显示发布时间
         */
        if (helper.layoutPosition > 0 && TimeUtils.getTime(item.msgTime) == TimeUtils.getTime(mDiscussList[helper.layoutPosition - 1].msgTime)) {
            helper.setVisible(R.id.create_time, View.GONE)
        } else {
            helper.setVisible(R.id.create_time, View.VISIBLE)
            helper.setText(R.id.create_time, TimeUtils.convertCurrentTime(mContext, item.msgTime))
        }
    }


    companion object {

        val ME = 1 //显示我自己
        val OTHER = ME + 1 //显示对方
    }
}
