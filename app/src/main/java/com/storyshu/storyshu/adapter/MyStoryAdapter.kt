package com.storyshu.storyshu.adapter

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.storyshu.storyshu.R
import com.storyshu.storyshu.info.CardInfo
import com.storyshu.storyshu.utils.time.TimeUtils
import com.storyshu.storyshu.widget.ClickButton
import com.storyshu.storyshu.widget.imageview.AvatarImageView

/**
 * 我的故事卡片的适配器
 * Created by bear on 2017/5/23.
 */

class MyStoryAdapter(myStoryList: List<CardInfo>?) :
        BaseQuickAdapter<CardInfo, MyStoryAdapter.ViewHold>(R.layout.my_story_item_layout, myStoryList) {

    interface OnStoryCardClickListener {
        fun onClick(position: Int)
    }


    override fun convert(helper: ViewHold?, item: CardInfo?) {
        if (helper!!.layoutPosition > 0) {
            if (TimeUtils.getDate(item?.createTime) == TimeUtils.getDate(getItem(helper.layoutPosition-1).createTime)) {

                helper?.day.visibility = View.GONE
                helper?.month.visibility = View.GONE
            } else {

                helper?.day.visibility = View.VISIBLE
                helper?.month.visibility = View.VISIBLE

                helper?.day.text = TimeUtils.getDay(item?.createTime)
                helper?.month.text = mContext.getString(R.string.unit_month, TimeUtils.getMonth(item?.createTime))
            }

        } else {

            helper?.day.visibility = View.VISIBLE
            helper?.month.visibility = View.VISIBLE

            helper?.day.text = TimeUtils.getDay(item?.createTime)
            helper?.month.text = mContext.getString(R.string.unit_month, TimeUtils.getMonth(item?.createTime))
        }

        Glide.with(mContext).load(item?.userInfo?.avatar).into(helper?.avatar)
        helper?.nickName.text = item?.userInfo?.nickname

        helper?.location.text = item?.locationTitle
        helper?.destoryTime.text = TimeUtils.convertDestroyTime(mContext, item?.destroyTime)

        helper?.like.num = item?.likeNum ?:0
        helper?.oppose.num = item?.opposeNum?:0
        helper?.comment.num = item?.commentNum?:0

        if (TextUtils.isEmpty(item?.cover)) {
            helper?.cover.visibility = View.GONE
        } else {
            helper?.cover.visibility = View.VISIBLE
            Glide.with(mContext).load(item?.cover).into(helper?.cover)
        }

        when (item?.storyType) {
            CardInfo.STORY -> {
                helper?.content.setTextColor(mContext.resources.getColor(R.color.colorBlack))
                helper?.content.text = item?.content
            }

            CardInfo.ARTICLE -> {
                helper?.content.setTextColor(mContext.resources.getColor(R.color.colorRedLight))
                helper?.content.text = mContext.getString(R.string.story_type_article, item?.title)
            }
        }
    }


    inner class ViewHold(itemView: View) : BaseViewHolder(itemView) {
        val day: TextView
        val month: TextView

        val avatar: AvatarImageView //用户头像
        val nickName: TextView //用户昵称
        val content: TextView
        val location: TextView
        val cover: ImageView
        val destoryTime: TextView

        val like: ClickButton //点赞
        val oppose: ClickButton  //喝倒彩
        val comment: ClickButton  //喝倒彩

        init {
            day = itemView.findViewById(R.id.day) as TextView
            month = itemView.findViewById(R.id.month) as TextView
            avatar = itemView.findViewById(R.id.avatar) as AvatarImageView
            nickName = itemView.findViewById(R.id.nickname) as TextView
            like = itemView.findViewById(R.id.like) as ClickButton
            oppose = itemView.findViewById(R.id.oppose) as ClickButton
            comment = itemView.findViewById(R.id.comment) as ClickButton
            destoryTime = itemView.findViewById(R.id.destroy_time) as TextView
            content = itemView.findViewById(R.id.content) as TextView
            cover = itemView.findViewById(R.id.cover_pic) as ImageView
            location = itemView.findViewById(R.id.location) as TextView
        }

    }
}
