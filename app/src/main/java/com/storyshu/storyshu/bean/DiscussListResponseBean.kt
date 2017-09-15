package com.storyshu.storyshu.bean

import com.storyshu.storyshu.info.MessageInfo

/**
 *
 * Created by bear on 2017/9/8.
 */
data class DiscussListResponseBean(
        var data: List<MessageInfo>,
        var code: Int = 0,
        var message: String = ""
) {
}