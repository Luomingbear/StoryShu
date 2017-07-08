package com.storyshu.storyshu.info

import android.os.Parcel
import android.os.Parcelable

import com.amap.api.maps.model.LatLng
import com.storyshu.storyshu.utils.ListUtil

/**
 * 故事信息
 * Created by bear on 2016/12/27.
 */

class StoryInfo : Parcelable{
    var storyId: String? = null //故事的id

    var content: String? = null //故事的内容

    var cover: String? = null //故事的封面图

    var storyPic: List<String>? = null //配图

    var userInfo: BaseUserInfo? = null //用户信息

    var createTime: String? = null //发布时间

    var destroyTime: String? = null //保存时间，单位分钟

    private var Content: String? = null //正文

    var latLng: LatLng? = null //坐标

    var location: String? = null //地点描述

    var isAnonymous = false //是否匿名

    constructor() {}

    constructor(storyId: String, cover: String, storyPic: List< String >, content: String,
    userInfo: BaseUserInfo, createDate: String, destroyTime: String, content1: String,
    latLng: LatLng, location: String, isAnonymous: Boolean) {
        this.storyId = storyId
        this.cover = cover
        this.storyPic = storyPic
        this.content = content
        this.userInfo = userInfo
        this.createTime = createDate
        this.destroyTime = destroyTime
        Content = content1
        this.latLng = latLng
        this.location = location
        this.isAnonymous = isAnonymous
    }

    fun setStoryPic(storyPic: String) {
        this.storyPic = ListUtil.StringToStringList(storyPic)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<StoryInfo> = object : Parcelable.Creator<StoryInfo> {
            override fun createFromParcel(source: Parcel): StoryInfo = StoryInfo(source)
            override fun newArray(size: Int): Array<StoryInfo?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {

    }
}
