package com.storyshu.storyshu.info

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by bear on 2017/9/16.
 */
data class MessageTypeInfo(var type: Int = MessageInfo.DISCUSS) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MessageTypeInfo> = object : Parcelable.Creator<MessageTypeInfo> {
            override fun createFromParcel(source: Parcel): MessageTypeInfo = MessageTypeInfo(source)
            override fun newArray(size: Int): Array<MessageTypeInfo?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(type)
    }
}