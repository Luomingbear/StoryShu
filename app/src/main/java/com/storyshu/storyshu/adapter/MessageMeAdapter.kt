package com.storyshu.storyshu.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.storyshu.storyshu.R
import com.storyshu.storyshu.info.StoryMessageInfo
import com.storyshu.storyshu.utils.time.TimeUtils

/**
 * Created by bear on 2017/9/16.
 */
class MessageMeAdapter(var context: Context, list: List<StoryMessageInfo>) :
        BaseQuickAdapter<StoryMessageInfo, BaseViewHolder>(R.layout.message_child_layout, list) {
    override fun convert(helper: BaseViewHolder, item: StoryMessageInfo) {
        Glide.with(context).load(item?.userInfo?.avatar).into(helper.getView(R.id.avatar))
        helper.setText(R.id.nickname, item.userInfo.nickname)
        helper.setText(R.id.create_time, TimeUtils.convertCurrentTime(mContext, item.createTime))

        when (item.messageType) {
            StoryMessageInfo.MessageType.LIKE_STORY -> helper.setText(R.id.content, R.string.like_my_story)
            StoryMessageInfo.MessageType.LIKE_COMMENT -> helper.setText(R.id.comment, R.string.like_my_comment)
            StoryMessageInfo.MessageType.COMMENT -> helper.setText(R.id.content, item.comment)
            StoryMessageInfo.MessageType.SYSTEM -> {
            }
        }

        if (TextUtils.isEmpty(item.getCover())) {
            helper.setVisible(R.id.extract, View.VISIBLE)
            helper.setVisible(R.id.cover, View.GONE)
            helper.setText(R.id.extract, item.storyContent)
        } else {
            helper.setVisible(R.id.extract, View.GONE)
            helper.setVisible(R.id.cover, View.VISIBLE)
            Glide.with(mContext).load(item.cover).into(helper.getView(R.id.cover))
        }
    }
}