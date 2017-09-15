package com.storyshu.storyshu.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.storyshu.storyshu.info.BaseUserInfo
import com.storyshu.storyshu.model.UserModel

/**
 * 自动获取昵称的Tv
 * Created by bear on 2017/9/15.
 */
class NickNameTextView : TextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setUserId(userId: Int) {
        val userMOdel = UserModel(context)
        userMOdel.getUserInfo(userId)
        userMOdel.setOnUserInfoGetListener(object : UserModel.OnUserInfoGetListener {
            override fun onSucceed(userInfo: BaseUserInfo?) {
                setText(userInfo?.nickname)
            }

            override fun onFailed(error: String?) {
                setText("SomeBody")
            }
        })
    }
}