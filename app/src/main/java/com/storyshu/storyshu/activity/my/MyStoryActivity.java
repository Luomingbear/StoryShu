package com.storyshu.storyshu.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.activity.story.StoryDetailActivity;
import com.storyshu.storyshu.adapter.MyStoryAdapter;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.model.database.StoryDateBaseHelper;
import com.storyshu.storyshu.utils.ParcelableUtil;
import com.storyshu.storyshu.widget.title.TitleView;

import java.util.Date;
import java.util.List;

/**
 * 我的故事界面
 * Created by bear on 2017/1/18.
 */

public class MyStoryActivity extends IBaseActivity {
    private static final String TAG = "MyStoryActivity";
    private SwipeRefreshLayout mRefreshLayout; //下拉刷新控件
    private RecyclerView mRecyclerView; //我的故事列表
    private List<StoryInfo> mStoryList; //故事列表数据
    private MyStoryAdapter myStoryAdapter; //故事布局的适配器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story_layout);
        initView();
    }

    /**
     * 标题栏
     */
    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
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
     * 初始化
     */
    private void initView() {
        //标题栏
        initTitle();

        //下拉刷新
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.my_story_refresh);
        mRefreshLayout.setColorSchemeColors(getResources()
                .getColor(R.color.colorRedLight));

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO: 2017/1/18 获取服务器上的故事
                StoryDateBaseHelper dateBaseHelper = new StoryDateBaseHelper(MyStoryActivity.this);
//                mStoryList = new ArrayList<StoryInfo>();
                mStoryList = dateBaseHelper.getLocalStory();
//                mStoryList.get(0).getUserInfo().setNickname("Bear");

                //刷新数据
                myStoryAdapter.notifyDataSetChanged();
                Date date = new Date();
                Log.i(TAG, "onRefresh: time:" + date);
                //停止下拉刷新动画
                mRefreshLayout.setRefreshing(false);
            }
        });

        /**
         列表
         *
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.my_story_recycler_view);
        //设置layoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        StoryDateBaseHelper dateBaseHelper = new StoryDateBaseHelper(MyStoryActivity.this);
        mStoryList = dateBaseHelper.getLocalStory();
        myStoryAdapter = new MyStoryAdapter(this, mStoryList);
        myStoryAdapter.setOnStoryItemClickListener(onStoryItemClickListener);
        mRecyclerView.setAdapter(myStoryAdapter);
    }

    private MyStoryAdapter.OnStoryItemClickListener onStoryItemClickListener = new MyStoryAdapter.OnStoryItemClickListener() {
        @Override
        public void onStoryClick(StoryInfo storyInfo) {
            intentWithParcelable(StoryDetailActivity.class, ParcelableUtil.STORY, storyInfo);
        }
    };

}
