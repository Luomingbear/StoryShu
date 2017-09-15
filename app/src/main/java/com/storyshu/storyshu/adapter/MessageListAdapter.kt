package com.storyshu.storyshu.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.storyshu.storyshu.R
import com.storyshu.storyshu.info.MessageInfo
import com.storyshu.storyshu.widget.NickNameTextView

/**
 * 信息列表适配器
 * Created by bear on 2017/9/15.
 */
class MessageListAdapter(var context: Context, list: MutableList<MessageInfo>) :
        BaseQuickAdapter<MessageInfo, BaseViewHolder>(R.layout.message_item_layout, list) {

    init {
        multiTypeDelegate = object : MultiTypeDelegate<MessageInfo>() {
            override fun getItemType(t: MessageInfo?): Int {
                return t?.type ?: MessageInfo.DISCUSS
            }
        }

        multiTypeDelegate.registerItemType(MessageInfo.DISCUSS, R.layout.message_item_layout)
        multiTypeDelegate.registerItemType(MessageInfo.CHAT, R.layout.message_item_layout)
        multiTypeDelegate.registerItemType(MessageInfo.COMMENT, R.layout.message_top_layout)
        multiTypeDelegate.registerItemType(MessageInfo.LIKE, R.layout.message_top_layout)

    }

    override fun convert(helper: BaseViewHolder, item: MessageInfo) {
        when (helper.itemViewType) {
            MessageInfo.DISCUSS -> {
                helper.setText(R.id.cover, item.content)

                if (!item.cover.isNullOrEmpty())
                    Glide.with(context).load(item.cover).into(helper.getView<ImageView>(R.id.image))
                helper.getView<NickNameTextView>(R.id.title).setUserId(item.userId)
                helper.setText(R.id.content, item.content)
            }
            MessageInfo.CHAT -> {
                helper.setText(R.id.cover, item.content)
                if (!item.cover.isNullOrEmpty())
                    Glide.with(context).load(item.cover).into(helper.getView<ImageView>(R.id.image))
                helper.getView<NickNameTextView>(R.id.title).setUserId(item.userId)
                helper.setText(R.id.content, item.content)
            }
            MessageInfo.COMMENT -> {
                helper.getView<ImageView>(R.id.image).setBackgroundResource(R.drawable.message_comment)
                helper.setText(R.id.title, context.getString(R.string.comment2me))
                helper.setText(R.id.num, item.content)

            }
            MessageInfo.LIKE -> {
                helper.getView<ImageView>(R.id.image).setBackgroundResource(R.drawable.message_mylove)
                helper.setText(R.id.title, context.getString(R.string.like2me))
                helper.setText(R.id.num, item.content)

            }
        }

    }
}