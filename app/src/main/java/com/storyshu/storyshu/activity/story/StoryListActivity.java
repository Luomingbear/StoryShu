package com.storyshu.storyshu.activity.story;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.widget.title.TitleView;

/**
 * 故事列表的形式显示的界面
 * Created by bear on 2017/1/26.
 */

public class StoryListActivity extends IBaseActivity {
    private TitleView mTitleView; //标题栏
    private SwipeRefreshLayout mRefreshLayout; //刷新控件
    private RecyclerView mRecyclerView; //显示故事列表的recyclerView

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initView();
    }

    private void initView() {

        //标题栏
//        mTitleView = (TitleView) findViewById(R.roomId.title_view);
//
//        //刷新控件
//        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.roomId.story_list_refresh);
//
//        //显示故事集的控件
//        mRecyclerView = (RecyclerView) findViewById(R.roomId.story_list_recycler_view);


    }

}


