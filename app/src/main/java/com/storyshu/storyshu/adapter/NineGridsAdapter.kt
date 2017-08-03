package com.storyshu.storyshu.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.storyshu.storyshu.R
import com.storyshu.storyshu.widget.ninegrid.NineGridAdapter

/**
 * 九宫格图片的适配器
 * Created by bear on 2017/8/3.
 */
class NineGridsAdapter(context: Context, list: MutableList<String>) :
        NineGridAdapter(context, list) {

    override fun getCount(): Int {
        return if (list == null) 0 else list.size
    }

    override fun getUrl(positopn: Int): String {
        return list?.get(positopn)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(i: Int): View {
        val iv = ImageView(context)
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP)
        iv.setBackgroundColor(context.resources.getColor(R.color.colorGrayLight))
        Glide.with(context).load(getUrl(i)).into(iv)
        return iv
    }

}