package com.storyshu.storyshu.activity.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.info.StoryBaseInfo;
import com.storyshu.storyshu.utils.ParcelableUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.story.StoryEditView;
import com.storyshu.storyshu.widget.title.TitleView;

/**
 * 写故事的页面啊
 * Created by bear on 2016/12/19.
 */

public class CreateStoryActivity extends IBaseActivity {
    private TitleView mTitleView; //标题栏
    private StoryEditView mStoryEdit; //正文编辑栏

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_story);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //标题栏
        mTitleView = (TitleView) findViewById(R.id.title_view);
        initTitleView();

        //正文编辑()
        mStoryEdit = (StoryEditView) findViewById(R.id.story_edit);
        if (!TextUtils.isEmpty(ISharePreference.getContent(this)))
            mStoryEdit.setContent(ISharePreference.getContent(this));
    }

    /**
     * 标题栏的设置和响应事件
     */
    private void initTitleView() {
        mTitleView.setOnTitleClickListener(new TitleView.onTitleClickListener() {
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
                StoryBaseInfo storyBaseInfo = new StoryBaseInfo();
                storyBaseInfo.setContent(mStoryEdit.getContent());
                intentWithParcelable(CreateCoverActivity.class, ParcelableUtil.STORY, storyBaseInfo);
            }
        });
    }

    @Override
    protected void onStop() {
        ISharePreference.saveContent(CreateStoryActivity.this, mStoryEdit.getContent());
        super.onStop();
    }
}
