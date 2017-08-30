package com.storyshu.storyshu.widget.imageview

import android.content.Context
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.storyshu.storyshu.info.BaseUserInfo
import com.storyshu.storyshu.model.UserModel

/**
 * 聊天头像控件
 * 通过userid获取头像的网络地址显示
 * Created by bear on 2017/8/30.
 */
class ChatAvatarView(context: Context, attrs: AttributeSet) : RoundImageView(context, attrs) {
    private var mUserInfo: BaseUserInfo? = null


    fun setUserId(userId: Int,listener:UserModel.OnUserInfoGetListener) {
        val userMolde = UserModel(context)
        userMolde.getUserInfo(userId)
        userMolde.setOnUserInfoGetListener(object : UserModel.OnUserInfoGetListener {
            override fun onSucceed(userInfo: BaseUserInfo?) {
                mUserInfo = userInfo

                setAvatar()
                listener.onSucceed(userInfo)
            }

            override fun onFailed(error: String?) {
                listener.onFailed(error)
            }
        })
    }

    private fun setAvatar() {
        Glide.with(context).load(mUserInfo?.avatar).into(this)
    }

    public fun getUserInfo(): BaseUserInfo? {
        return mUserInfo
    }
}