package com.storyshu.storyshu.widget.ninegrid

import android.content.Context
import android.view.View

/**
 * Created by bear on 2017/8/3.
 */
abstract class NineGridAdapter(protected var context: Context, protected var list: List<String>) {

    abstract fun getCount(): Int

    abstract fun getUrl(positopn: Int): String

    abstract fun getItemId(position: Int): Long

    abstract fun getView(i: Int): View
}
