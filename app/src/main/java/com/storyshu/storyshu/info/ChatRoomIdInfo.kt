package com.storyshu.storyshu.info

import android.os.Parcel
import android.os.Parcelable

/**
 * 聊天室id实体类
 * Created by bear on 2017/8/30.
 */
data class ChatRoomIdInfo(
        var roomID: String = ""
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ChatRoomIdInfo> = object : Parcelable.Creator<ChatRoomIdInfo> {
            override fun createFromParcel(source: Parcel): ChatRoomIdInfo = ChatRoomIdInfo(source)
            override fun newArray(size: Int): Array<ChatRoomIdInfo?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(roomID)
    }
}