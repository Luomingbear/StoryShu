package com.storyshu.storyshu.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.storyshu.storyshu.R
import com.storyshu.storyshu.activity.base.IBaseActivity
import com.storyshu.storyshu.bean.getStory.StoryIdBean
import com.storyshu.storyshu.mvp.discuss.DiscussPresenterIml
import com.storyshu.storyshu.mvp.discuss.DiscussView
import com.storyshu.storyshu.utils.NameUtil
import com.storyshu.storyshu.utils.StatusBarUtils
import com.storyshu.storyshu.widget.title.TitleView
import kotlinx.android.synthetic.main.activity_discuss_layout.*

/**
 * 讨论页面
 * Created by bear on 2017/5/24.
 */

class DiscussActivity : IBaseActivity(), DiscussView, View.OnClickListener {


    private var mDiscussPresenterIml: DiscussPresenterIml? = null
    private var mStoryIdBean: StoryIdBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_discuss_layout)
        initView()

        initData()

        mDiscussPresenterIml = DiscussPresenterIml(this, this)

        initEvent()
    }

    private fun initView() {
        StatusBarUtils.setColor(this@DiscussActivity, R.color.colorRed)

    }

    /**
     * 初始化数据
     */
    private fun initData() {
        mStoryIdBean = intent.getParcelableExtra<StoryIdBean>(NameUtil.STORY_ID_BEAN)
    }

    /**
     * 初始化标题
     */
    private fun initTitle() {
        title_view.setOnTitleClickListener(object : TitleView.OnTitleClickListener {
            override fun onLeftClick() {
                onBackPressed()
            }

            override fun onCenterClick() {

            }

            override fun onCenterDoubleClick() {

            }

            override fun onRightClick() {}
        })

    }

    /**
     * 初始化输入框
     */
    private fun initEditText() {
        input_edit!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 0)
                    send.bgColor = R.color.colorRed
                else
                    send.bgColor = R.color.colorGrayLight
            }
        })

    }

    private fun initEvent() {
        initTitle()

        initEditText()

        send!!.setOnClickListener(this)

        mDiscussPresenterIml!!.initRefreshLayout()

        mDiscussPresenterIml!!.initDiscussData()

        mDiscussPresenterIml!!.addEMMListener()

    }

    override fun onDestroy() {
        super.onDestroy()
        mDiscussPresenterIml!!.removeEMMListener()
    }

    override fun showToast(s: String) {

    }

    override fun showToast(stringRes: Int) {

    }

    override fun getRefreshLayout(): SwipeRefreshLayout {
        return refresh_layout
    }

    override fun getDiscussRv(): RecyclerView {
        return listView
    }

    override fun getEditText(): EditText {
        return input_edit
    }

    override fun notifyData() {
        runOnUiThread {
            listView.adapter.notifyDataSetChanged()

            listView.scrollToPosition(listView.adapter.itemCount - 1)
        }
    }

    override fun getStoryId(): String {
        return mStoryIdBean!!.storyId
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.send -> mDiscussPresenterIml!!.sendMessage()
        }
    }
}
