package com.storyshu.storyshu.activity.story;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.bean.getStory.StoryBean;
import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.mvp.storyroom.StoryRoomPresenterIml;
import com.storyshu.storyshu.mvp.storyroom.StoryRoomView;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.ClickButton;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;
import com.storyshu.storyshu.widget.title.TitleView;

import java.util.List;

public class StoryRoomActivity extends AppCompatActivity implements StoryRoomView, View.OnClickListener {
    private static final String TAG = "StoryRoomActivity";
    private TitleView mTitleView; //标题栏
    private TextView mStoryContent; //故事的内容
    private ImageView mStoryCover; //故事的配图
    private TextView mPicSize; //配图的数量
    private TextView mLocation; //发布位置
    private TextView mReport; //举报
    private AvatarImageView mAvatar; //作者头像
    private TextView mNickname; //作者昵称
    private TextView mCreateTime; //发布时间
    private TextView mDeathTime; //剩余时间

    private ClickButton mLike, mOppose, mComment; //按钮

    private NestedScrollView mScrollView; //滚动布局
    private SwipeRefreshLayout mRefreshLayout; //下拉刷新控件
    private RecyclerView mCommentRV; //评论的列表控件

    private StoryBean mStoryBean; //故事的数据
    private StoryIdBean mStoryIdBean; //故事的id数据

    private StoryRoomPresenterIml mStoryRoomPresenter; //故事屋的控制

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_room_layout);

        initView();
        mStoryRoomPresenter = new StoryRoomPresenterIml(StoryRoomActivity.this, StoryRoomActivity.this);

        initData();

        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mStoryRoomPresenter.getComments();
    }

    /**
     * 初始化
     */
    @Override
    public void initView() {
        StatusBarUtils.setColor(this, R.color.colorRed);

        mTitleView = (TitleView) findViewById(R.id.title_view);

        mStoryContent = (TextView) findViewById(R.id.story_content);

        mStoryCover = (ImageView) findViewById(R.id.story_pic);

        mPicSize = (TextView) findViewById(R.id.pic_size);

        mLocation = (TextView) findViewById(R.id.location);

        mReport = (TextView) findViewById(R.id.report);

        mAvatar = (AvatarImageView) findViewById(R.id.author_avatar);

        mNickname = (TextView) findViewById(R.id.author_username);

        mCreateTime = (TextView) findViewById(R.id.create_time);

        mDeathTime = (TextView) findViewById(R.id.destroy_time);

        mLike = (ClickButton) findViewById(R.id.like);

        mOppose = (ClickButton) findViewById(R.id.oppose);

        mComment = (ClickButton) findViewById(R.id.comment_content);

//      mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        mScrollView = (NestedScrollView) findViewById(R.id.scroll_view);

        mCommentRV = (RecyclerView) findViewById(R.id.comment_list);

        //修复打开页面的自动滚动问题
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
            }
        });
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

            }
        });

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        mStoryBean = getIntent().getParcelableExtra(NameUtil.STORY_INFO);
        mStoryIdBean = getIntent().getParcelableExtra(NameUtil.STORY_ID_BEAN);
        if (mStoryBean != null) {
            setStoryData(mStoryBean);
        } else if (mStoryIdBean != null) {
            mStoryRoomPresenter.getStoryInfo();
        }
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
     * 点击了赞
     */
    private ClickButton.OnClickButtonListener likeClickListener = new ClickButton.OnClickButtonListener() {
        @Override
        public void onCLicked(boolean isClicked) {
            mStoryRoomPresenter.clickLike();
        }
    };

    /**
     * 点击了喝倒彩
     */
    private ClickButton.OnClickButtonListener opposeClickListener = new ClickButton.OnClickButtonListener() {
        @Override
        public void onCLicked(boolean isClicked) {
            mStoryRoomPresenter.clickOppose();
        }
    };

    /**
     * 点击了评论
     */
    private ClickButton.OnClickButtonListener commentClickListener = new ClickButton.OnClickButtonListener() {
        @Override
        public void onCLicked(boolean isClicked) {
            mStoryRoomPresenter.clickComment();
        }
    };

    /**
     * 初始化事件处理
     */
    private void initEvent() {
        initTitle();

        mStoryCover.setOnClickListener(this);

        mReport.setOnClickListener(this);

        mLike.setOnClickListener(this);

        mOppose.setOnClickListener(this);

        mComment.setOnClickListener(this);

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

            case R.id.like:
                mStoryRoomPresenter.clickLike();
                break;
            case R.id.oppose:
                mStoryRoomPresenter.clickOppose();
                break;
            case R.id.comment:
                mStoryRoomPresenter.clickComment();
                break;
        }
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
    public RecyclerView getCommentRV() {
        return mCommentRV;
    }

    @Override
    public List<String> getStoryPic() {
        return mStoryBean.getStoryPictures();
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

        mStoryContent.setText(storyData.getContent());

        if (!TextUtils.isEmpty(storyData.getCover())) {
            Glide.with(this).load(storyData.getCover()).into(mStoryCover);

            if (storyData.getStoryPictures().size() > 1)
                mPicSize.setText(storyData.getStoryPictures().size() + "");
            else mPicSize.setVisibility(View.GONE);

        } else {
            mStoryCover.setVisibility(View.GONE);
            mPicSize.setVisibility(View.GONE);
        }

        /**
         * 检测是否是匿名的
         */
        if (storyData.getAnonymous()) {
            findViewById(R.id.story_detail_layout).setBackgroundResource(R.drawable.card_view_black_bg);
            findViewById(R.id.line).setVisibility(View.GONE);
            mStoryContent.setTextColor(getResources().getColor(R.color.colorWhiteDeep));
            mLocation.setTextColor(getResources().getColor(R.color.colorGray));
            mReport.setTextColor(getResources().getColor(R.color.colorGray));
            mAvatar.setImageResource(R.drawable.avatar_wolverine);
            mNickname.setVisibility(View.GONE);
        } else {
            Glide.with(this).load(storyData.getUserInfo().getAvatar()).into(mAvatar);
            mNickname.setText(storyData.getUserInfo().getNickname());
        }

        mLocation.setText(storyData.getLocationTitle());

        mCreateTime.setText(TimeUtils.convertCurrentTime(this, storyData.getCreateTime()));

        mDeathTime.setText(TimeUtils.leftTime(this, storyData.getDestroyTime()));

        mLike.setNum(storyData.getLikeNum());
        mOppose.setNum(storyData.getOpposeNum());
    }

    @Override
    protected void onDestroy() {
        mStoryRoomPresenter.distach();
        super.onDestroy();
    }
}
