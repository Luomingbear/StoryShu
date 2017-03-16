package com.storyshu.storyshu.activity.story;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.CommentAdapter;
import com.storyshu.storyshu.info.CommentInfo;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.time.TimeConvertUtil;
import com.storyshu.storyshu.widget.ClickButton;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;
import com.storyshu.storyshu.widget.title.TitleView;

import java.util.ArrayList;
import java.util.Date;

public class StoryRoomActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "StoryRoomActivity";
    private TitleView mTitleView; //标题栏
    private TextView mStoryContent; //故事的内容
    private ImageView mStoryPic; //故事的配图
    private TextView mPicSize; //配图的数量
    private TextView mLocation; //发布位置
    private View mReport; //举报
    private AvatarImageView mAvatar; //作者头像
    private TextView mUsername; //作者昵称
    private TextView mCreateTime; //发布时间
    private TextView mDeathTime; //剩余时间

    private ClickButton mLike, mOppose, mComment; //按钮
    private int isLike = 0; //是否点赞 0:不操作 1：点赞 -1：喝倒彩

    private NestedScrollView mScrollView; //滚动布局
    private SwipeRefreshLayout mRefreshLayout; //下拉刷新控件
    private RecyclerView mCommentRV; //评论的列表控件

    private ArrayList<CommentInfo> mCommentInfoList; //评论的数据源
    private CommentAdapter mCommentAdapter; //评论适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_room_layout);

        initView();

        initData();

        initEvent();

    }

    /**
     * 初始化
     */
    private void initView() {
        mTitleView = (TitleView) findViewById(R.id.title_view);

        mStoryContent = (TextView) findViewById(R.id.story_content);

        mStoryPic = (ImageView) findViewById(R.id.story_pic);

        mPicSize = (TextView) findViewById(R.id.pic_size);

        mLocation = (TextView) findViewById(R.id.location);

        mReport = findViewById(R.id.report);

        mAvatar = (AvatarImageView) findViewById(R.id.author_avatar);

        mUsername = (TextView) findViewById(R.id.author_username);

        mDeathTime = (TextView) findViewById(R.id.destroy_time);

        mLike = (ClickButton) findViewById(R.id.like);

        mOppose = (ClickButton) findViewById(R.id.oppose);

        mComment = (ClickButton) findViewById(R.id.comment_content);

//        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        mScrollView = (NestedScrollView) findViewById(R.id.scroll_view);

        mCommentRV = (RecyclerView) findViewById(R.id.comment_list);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
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
     * 初始化评论数据
     */
    private void initComments() {
        mCommentInfoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            CommentInfo commentInfo = new CommentInfo();
            commentInfo.setNickname("赵日天");
            commentInfo.setAvatar("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3022635415,3979946006&fm=23&gp=0.jpg");
            commentInfo.setCreateTime(TimeConvertUtil.convertCurrentTime(new Date(System.currentTimeMillis())));
            commentInfo.setOpposeNum(23);
            commentInfo.setLikeNum(66);
            commentInfo.setTags(i + "#");
            commentInfo.setComment("我赵日天表示不服,你怕了吗？");
            mCommentInfoList.add(commentInfo);
        }

        mCommentInfoList.get(3).setComment("我赵日天表示不服,你怕了吗？我赵日天表示不服,你怕了吗？我赵日天表示不服,你怕了吗？");
        mCommentInfoList.get(9).setComment("我赵日天表示不服,你怕了吗？我赵日天表示不服,你怕了吗？我赵日天表示不服,你怕了吗？");

        mCommentAdapter = new CommentAdapter(StoryRoomActivity.this, mCommentInfoList);

        //layoutmanager
        mCommentRV.setLayoutManager(new LinearLayoutManager(this));

        //设置数据
        mCommentRV.setAdapter(mCommentAdapter);

        //修复高度问题
//        AdapterViewUtil.FixHeight(mCommentRV, this);
        //修复打开页面的自动滚动问题
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initComments();
    }

    /**
     * 点击了赞
     */
    private ClickButton.OnClickButtonListener likeClickListener = new ClickButton.OnClickButtonListener() {
        @Override
        public void onCLicked(boolean isClicked) {

        }
    };

    /**
     * 点击了喝倒彩
     */
    private ClickButton.OnClickButtonListener opposeClickListener = new ClickButton.OnClickButtonListener() {
        @Override
        public void onCLicked(boolean isClicked) {

        }
    };

    /**
     * 点击了评论
     */
    private ClickButton.OnClickButtonListener commentClickListener = new ClickButton.OnClickButtonListener() {
        @Override
        public void onCLicked(boolean isClicked) {

        }
    };


    /**
     * 初始化点赞、评论、喝倒彩按钮
     */
    private void initCheckTextClick() {
        mLike.setOnClickButtonListener(likeClickListener);
        mOppose.setOnClickButtonListener(opposeClickListener);
        mComment.setOnClickButtonListener(commentClickListener);
    }

    /**
     * 初始化事件处理
     */
    private void initEvent() {
        initTitle();

        mStoryPic.setOnClickListener(this);

        mReport.setOnClickListener(this);

        mLike.setOnClickListener(this);

        initCheckTextClick();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.story_pic:
                ToastUtil.Show(StoryRoomActivity.this, "Click Picture");
                break;

            case R.id.report:
                ToastUtil.Show(StoryRoomActivity.this, R.string.report);
                break;
        }
    }
}
