package com.storyshu.storyshu.widget.dialog

import android.content.Context
import com.storyshu.storyshu.R

/**
 *
 * Created by bear on 2017/8/30.
 */
class LoadingDialog(context: Context, themeResId: Int) :
        IBaseDialog(context, themeResId) {
    override fun getLayoutRes(): Int {
        return R.layout.loading_dialog
    }

    override fun initView() {

    }
}