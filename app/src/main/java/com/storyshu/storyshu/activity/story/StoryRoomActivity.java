package com.storyshu.storyshu.activity.story;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.DiscussActivity;
import com.storyshu.storyshu.activity.UserIntroductionActivity;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.adapter.NineGridsAdapter;
import com.storyshu.storyshu.adapter.base.OnItemClickListener;
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
import com.storyshu.storyshu.widget.card.AutoScaleLayout;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;
import com.storyshu.storyshu.widget.ninegrid.NineGridlayout;
import com.storyshu.storyshu.widget.text.RichTextEditor;
import com.storyshu.storyshu.widget.text.RoundTextView;
import com.storyshu.storyshu.widget.title.TitleView;

import java.util.List;

public class StoryRoomActivity extends IBaseActivity implements StoryRoomView, View.OnClickListener {
    private static final String TAG = "StoryRoomActivity";
    private TitleView mTitleView; //标题栏
    private LinearLayout mStoryLayout; //故事显示的布局
    private RichTextEditor mRichTextEditor; //富文本显示框架
    private TextView mStoryContent; //故事的内容
    private NineGridlayout mNineGridLayout; //故事的配图
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

        setContentView(getLayoutRes());

        initView();

        initData();

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

        mStoryLayout = (LinearLayout) findViewById(R.id.story_detail_layout);

//        /**
//         * 短文界面特有
//         */
//
//        if (mStoryBean != null) {
//            switch (mStoryBean.getStoryType()) {
//                case CardInfo.STORY:
//                    mStoryCover = (ImageView) findViewById(R.id.story_pic);
//                    mStoryContent = (TextView) findViewById(R.id.story_content);
//                    break;
//
//                case CardInfo.ARTICLE:
//                    mTitle = (TextView) findViewById(R.id.title_tv);
//                    break;
//            }
//        }

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
            setStoryData(new StoryBean((CardInfo) getIntent().getParcelableExtra(NameUtil.CARD_INFO)));
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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

        /**
         * 首次获取到数据需要设置布局
         */
        if (mStoryBean == null) {
            mStoryBean = storyData;
            setLayout();
        } else mStoryBean = storyData;

        //
        switch (mStoryBean.getStoryType()) {
            case CardInfo.STORY:
                mStoryContent.setText(storyData.getContent());

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

    /**
     * 根据数据类型生产不同的布局
     */
    private void setLayout() {
        switch (mStoryBean.getStoryType()) {
            case CardInfo.STORY:
                createStoryLayout();
                break;
            case CardInfo.ARTICLE:
                createArticleLayout();
                break;
        }

    }

    /**
     * 生成故事的布局
     */
    private void createStoryLayout() {

        //内容
        mStoryContent = new TextView(StoryRoomActivity.this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.margin_normal);
        mStoryContent.setLayoutParams(params);
        mStoryContent.setPadding(margin, margin, margin, 0);
        mStoryContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
        mStoryContent.setTextColor(getResources().getColor(R.color.colorBlack));
        mStoryContent.setLineSpacing(getResources().getDimension(R.dimen.line_space_normal), 1);
        mStoryLayout.addView(mStoryContent, 0);

        AutoScaleLayout scaleLayout = new AutoScaleLayout(StoryRoomActivity.this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.setMargins(margin, margin, margin, margin);
        scaleLayout.setLayoutParams(params2);
        scaleLayout.setScaleRate(1);


        //九宫格
        mNineGridLayout = new NineGridlayout(StoryRoomActivity.this);
        mNineGridLayout.setLayoutParams(params);

        mNineGridLayout.setOnItemClickListener(onImageClickListener);

        scaleLayout.addView(mNineGridLayout);

        if (!TextUtils.isEmpty(mStoryBean.getCover()))
            mStoryLayout.addView(scaleLayout, 1);
    }

    private OnItemClickListener onImageClickListener = new OnItemClickListener() {
        @Override
        public void onClick(int... args) {
            mStoryRoomPresenter.showStoryPicDialog(args[0]);
        }
    };

    /**
     * 生成文章的布局
     */
    private void createArticleLayout() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mTitle = new TextView(StoryRoomActivity.this);
        mTitle.setLayoutParams(params);
        int padding = (int) getResources().getDimension(R.dimen.margin_normal);
        mTitle.setPadding(padding, padding, padding, padding);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_title));
        mTitle.setLineSpacing(getResources().getDimension(R.dimen.line_space_normal), 1);
        mTitle.setTextColor(getResources().getColor(R.color.colorBlackLight));

        mStoryLayout.addView(mTitle, 0);

        mRichTextEditor = new RichTextEditor(StoryRoomActivity.this);
        mRichTextEditor.setLayoutParams(params);

        mStoryLayout.addView(mRichTextEditor, 1);
    }

    @Override
    public void setStoryPic(List<String> storyPics) {
        mStoryBean.setStoryPictures(storyPics);

        NineGridsAdapter adapter = new NineGridsAdapter(this, storyPics);
        mNineGridLayout.setAdapter(adapter);
    }

    @Override
    public void intent2Discuss() {
        intentWithParcelable(DiscussActivity.class, NameUtil.STORY_ID_BEAN, new StoryIdBean(mStoryBean.getStoryId()));
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
