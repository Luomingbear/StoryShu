package com.storyshu.storyshu.activity.story;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.DiscussActivity;
import com.storyshu.storyshu.activity.UserIntroductionActivity;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.bean.getStory.StoryBean;
import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.mvp.storyroom.StoryRoomPresenterIml;
import com.storyshu.storyshu.mvp.storyroom.StoryRoomView;
import com.storyshu.storyshu.utils.KeyBordUtil;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.ClickButton;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;
import com.storyshu.storyshu.widget.text.RichTextEditor;
import com.storyshu.storyshu.widget.text.RoundTextView;
import com.storyshu.storyshu.widget.title.TitleView;

import java.util.List;

public class StoryRoomActivity extends IBaseActivity implements StoryRoomView, View.OnClickListener {
    private static final String TAG = "StoryRoomActivity";
    private TitleView mTitleView; //标题栏
    private RichTextEditor mRichTextEditor; //富文本显示框架
    private TextView mStoryContent; //故事的内容
    private ImageView mStoryCover; //故事的配图
    private TextView mPicSize; //配图的数量
    private TextView mLocation; //发布位置
    private TextView mReport; //举报
    private AvatarImageView mAvatar; //作者头像
    private TextView mNickname; //作者昵称
    private TextView mCreateTime; //发布时间
    private TextView mDeathTime; //剩余时间
    private TextView mTitle; //标题

    private ClickButton mLike, mOppose, mComment; //按钮
    private RoundTextView mSend; //发送按钮

    private NestedScrollView mScrollView; //滚动布局
    private SwipeRefreshLayout mRefreshLayout; //下拉刷新控件
    private RecyclerView mCommentRV; //评论的列表控件

    private StoryBean mStoryBean; //故事的数据
    private StoryIdBean mStoryIdBean; //故事的id数据

    private StoryRoomPresenterIml mStoryRoomPresenter; //故事屋的控制

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

        setContentView(getLayoutRes());

        initView();

        initEvent();
    }


    /**
     * 获取需要加载的视图信息
     *
     * @return
     */
    private int getLayoutRes() {
        if (mStoryBean == null)
            return R.layout.story_room_layout;

        switch (mStoryBean.getStoryType()) {
            case CardInfo.STORY: //短文
                return R.layout.story_room_layout;

            case CardInfo.ARTICLE: //文章
                return R.layout.activity_story_room_long_layout;

            case CardInfo.VIDEO: //视频
                return R.layout.story_room_layout;
        }

        return R.layout.story_room_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mStoryRoomPresenter.updateComments();
    }

    /**
     * 初始化
     */
    @Override
    public void initView() {
        StatusBarUtils.setColor(this, R.color.colorRed);

        mTitleView = (TitleView) findViewById(R.id.title_view);

        mRichTextEditor = (RichTextEditor) findViewById(R.id.rich_text_edit);


        mPicSize = (TextView) findViewById(R.id.pic_size);

        mLocation = (TextView) findViewById(R.id.location);

        mReport = (TextView) findViewById(R.id.report);

        mAvatar = (AvatarImageView) findViewById(R.id.author_avatar);

        mNickname = (TextView) findViewById(R.id.author_username);

        mCreateTime = (TextView) findViewById(R.id.create_time);

        mDeathTime = (TextView) findViewById(R.id.destroy_time);

        mLike = (ClickButton) findViewById(R.id.like);

        mOppose = (ClickButton) findViewById(R.id.oppose);

        mComment = (ClickButton) findViewById(R.id.comment);

        mSend = (RoundTextView) findViewById(R.id.send);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorRedLight, R.color.colorRed);

        mScrollView = (NestedScrollView) findViewById(R.id.scroll_view);

        mCommentRV = (RecyclerView) findViewById(R.id.comment_list);

        /**
         * 短文界面特有
         */
        if (mStoryBean.getStoryType() == CardInfo.STORY) {
        }

        switch (mStoryBean.getStoryType()) {
            case CardInfo.STORY:
                mStoryCover = (ImageView) findViewById(R.id.story_pic);
                mStoryContent = (TextView) findViewById(R.id.story_content);
                break;

            case CardInfo.ARTICLE:
                mTitle = (TextView) findViewById(R.id.title_tv);
                break;
        }

        //修复打开页面的自动滚动问题
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);

                //隐藏键盘
                KeyBordUtil.hideKeyboard(StoryRoomActivity.this, mTitleView);
            }
        });

        mStoryRoomPresenter = new StoryRoomPresenterIml(StoryRoomActivity.this, StoryRoomActivity.this);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                onBackPressed();
            }

            @Override
            public void onCenterClick() {

            }

            @Override
            public void onCenterDoubleClick() {

            }

            @Override
            public void onRightClick() {
                intent2Discuss();
            }
        });

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        if (getIntent().getParcelableExtra(NameUtil.CARD_INFO) != null)
            mStoryBean = new StoryBean((CardInfo) getIntent().getParcelableExtra(NameUtil.CARD_INFO));
        mStoryIdBean = getIntent().getParcelableExtra(NameUtil.STORY_ID_BEAN);
    }

    @Override
    public void initEvents() {

    }

    @Override
    public void showToast(String s) {
        ToastUtil.Show(StoryRoomActivity.this, s);
    }

    @Override
    public void showToast(int stringRes) {
        ToastUtil.Show(StoryRoomActivity.this, stringRes);
    }

    /**
     * 初始化评论输入框
     */
    private void initCommentEdit() {
        getCommentEdit().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    mSend.setBgColor(R.color.colorRed);
                else
                    mSend.setBgColor(R.color.colorGrayLight);
            }
        });

        findViewById(R.id.hide_keyboard_layout).setOnClickListener(this);
    }

    /**
     * 初始化事件处理
     */
    private void initEvent() {

        if (mStoryBean != null) {
            setStoryData(mStoryBean);
        } else if (mStoryIdBean != null) {
            mStoryRoomPresenter.getStoryInfo();
        }

        //
        initTitle();

        mReport.setOnClickListener(this);

        mAvatar.setOnClickListener(this);

        mLike.setOnClickListener(this);

        mOppose.setOnClickListener(this);

        mSend.setOnClickListener(this);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mStoryRoomPresenter.getStoryInfo();
            }
        });

        initCommentEdit();

        if (mStoryBean.getStoryType() == CardInfo.STORY)
            mStoryCover.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.story_pic:
                mStoryRoomPresenter.showStoryPicDialog();
                break;

            case R.id.report:
                ToastUtil.Show(StoryRoomActivity.this, R.string.report);
                break;

            case R.id.author_avatar:
                intent2UserInfo();
                break;

            case R.id.like:
                mStoryRoomPresenter.clickLike();
                break;
            case R.id.oppose:
                mStoryRoomPresenter.clickOppose();
                break;

            case R.id.send:
                mStoryRoomPresenter.clickSend();
                break;

            case R.id.hide_keyboard_layout:
                KeyBordUtil.hideKeyboard(StoryRoomActivity.this, getCommentEdit());
                break;
        }
    }

    @Override
    public SwipeRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    @Override
    public ClickButton getLikeButton() {
        return mLike;
    }

    @Override
    public ClickButton getOpposeButton() {
        return mOppose;
    }

    @Override
    public ClickButton getCommentButton() {
        return mComment;
    }

    @Override
    public EditText getCommentEdit() {
        return (EditText) findViewById(R.id.edit_comment);
    }

    @Override
    public RoundTextView getSendButton() {
        return (RoundTextView) findViewById(R.id.send);
    }

    @Override
    public RecyclerView getCommentRV() {
        return mCommentRV;
    }

    @Override
    public TextView getHotCommentHit() {
        return (TextView) findViewById(R.id.hot_comment_hit);
    }

    @Override
    public List<String> getStoryPic() {
        return mStoryBean == null ? null : mStoryBean.getStoryPictures();
    }

    @Override
    public String getStoryId() {
        if (mStoryIdBean != null)
            return mStoryIdBean.getStoryId();
        if (mStoryBean != null)
            return mStoryBean.getStoryId();
        return "";
    }

    @Override
    public void setStoryData(StoryBean storyData) {
        if (storyData == null)
            return;
        mStoryBean = storyData;


        switch (mStoryBean.getStoryType()) {
            case CardInfo.STORY:
                mStoryContent.setText(storyData.getContent());

                if (!TextUtils.isEmpty(storyData.getCover())) {
                    mStoryCover.setVisibility(View.VISIBLE);
                    Glide.with(this).load(storyData.getCover()).dontAnimate().into(mStoryCover);

                    if (storyData.getStoryPictures() != null && storyData.getStoryPictures().size() > 1) {
                        mPicSize.setVisibility(View.VISIBLE);
                        mPicSize.setText(storyData.getStoryPictures().size() + "");
                    } else mPicSize.setVisibility(View.GONE);

                } else {
                    mStoryCover.clearAnimation(); //清除动画
                    mStoryCover.setVisibility(View.GONE);
                    mPicSize.setVisibility(View.GONE);
                }

                /**
                 * 获取配图
                 */
                mStoryRoomPresenter.getStoryPic();
                break;

            case CardInfo.ARTICLE:
                mRichTextEditor.parseXml(mStoryBean.getContent());
                mTitle.setText(mStoryBean.getTitle());
                break;
        }

        /**
         * 检测是否是匿名的
         */
        if (storyData.getAnonymous()) {
//            findViewById(R.id.story_detail_layout).setBackgroundResource(R.drawable.card_view_black_bg);
//            findViewById(R.id.line).setVisibility(View.GONE);
//            mStoryContent.setTextColor(getResources().getColor(R.color.colorWhiteDeep));
//            mLocation.setTextColor(getResources().getColor(R.color.colorGray));
//            mReport.setTextColor(getResources().getColor(R.color.colorGray));
            mAvatar.setImageResource(R.drawable.avatar_wolverine);
            mNickname.setVisibility(View.GONE);
        } else {
            Glide.with(this).load(storyData.getUserInfo().getAvatar()).into(mAvatar);
            mNickname.setText(storyData.getUserInfo().getNickname());
        }
        mLocation.setText(storyData.getLocationTitle());

        mCreateTime.setText(TimeUtils.convertCurrentTime(this, storyData.getCreateTime()));

        mDeathTime.setText(TimeUtils.convertDestroyTime(this, storyData.getDestroyTime()));

        if (TimeUtils.isOutOfDate(storyData.getDestroyTime())) {
            findViewById(R.id.input_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.input_layout).setVisibility(View.VISIBLE);
        }

        mLike.setNum(storyData.getLikeNum());
        mOppose.setNum(storyData.getOpposeNum());
    }

    @Override
    public void setStoryPic(List<String> storyPics) {
        mStoryBean.setStoryPictures(storyPics);

        if (mStoryBean.getStoryPictures() != null && mStoryBean.getStoryPictures().size() > 1) {
            mPicSize.setVisibility(View.VISIBLE);
            mPicSize.setText(mStoryBean.getStoryPictures().size() + "");
        } else mPicSize.setVisibility(View.GONE);
    }

    @Override
    public void intent2Discuss() {
        intentTo(DiscussActivity.class);
    }

    @Override
    public void intent2UserInfo() {
        intentTo(UserIntroductionActivity.class);
    }

    @Override
    protected void onDestroy() {
        mStoryRoomPresenter.distach();
        super.onDestroy();
    }
}
