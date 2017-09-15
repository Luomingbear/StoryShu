package com.storyshu.storyshu.mvp.message_me

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.storyshu.storyshu.R
import com.storyshu.storyshu.activity.base.IBaseActivity
import com.storyshu.storyshu.activity.story.StoryRoomActivity
import com.storyshu.storyshu.bean.getStory.StoryIdBean
import com.storyshu.storyshu.info.MessageInfo
import com.storyshu.storyshu.info.MessageTypeInfo
import com.storyshu.storyshu.utils.NameUtil
import com.storyshu.storyshu.utils.ToastUtil
import com.storyshu.storyshu.widget.title.TitleView
import kotlinx.android.synthetic.main.activity_my_story.*

/**
 * 点赞／评论列表
 * Created by bear on 2017/9/16.
 */
class MessageMeActivity : IBaseActivity(), MessageMeView {
    private var mMessageTypeInfo: MessageTypeInfo? = null
    private var mPresenter: MessageMePresenter? = null
    override fun getMessageType(): Int {
        return mMessageTypeInfo?.type ?: MessageInfo.DISCUSS
    }

    override fun showToast(s: String?) {
        ToastUtil.Show(this, s)
    }

    override fun showToast(stringRes: Int) {
        ToastUtil.Show(this, stringRes)

    }

    override val messageRv: RecyclerView
        get() = my_story_rv
    override val refreshLayout: SwipeRefreshLayout
        get() = refresh_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_story)


        initData()

        initView()

        initEvent()

    }

    fun initData() {
        mMessageTypeInfo = intent.getParcelableExtra(NameUtil.MESSAGE_TYPE_INFO)

    }


    fun initView() {
        if (mMessageTypeInfo != null) {
            when (mMessageTypeInfo?.type) {
                MessageInfo.LIKE -> {
                    title_view.setTitleString(getString(R.string.like2me))
                }

                MessageInfo.COMMENT -> {
                    title_view.setTitleString(getString(R.string.comment2me))
                }
            }
        }

        mPresenter = MessageMePresenter(this, this)
    }


    fun initEvent() {
        title_view.setTitleString(getString(R.string.comment2me))
        title_view.setOnTitleClickListener(object : TitleView.OnTitleClickListener {
            override fun onLeftClick() {
                onBackPressed()
            }

            override fun onCenterClick() {
            }

            override fun onCenterDoubleClick() {
            }

            override fun onRightClick() {
            }
        })

        mPresenter?.initRefreshLayout()

        mPresenter?.initRv()

        mPresenter?.getRvData()

    }

    override fun intentStoryRoom(storyIdBean: StoryIdBean) {
        intentWithParcelable(StoryRoomActivity::class.java, NameUtil.STORY_ID_BEAN, storyIdBean)
    }
}