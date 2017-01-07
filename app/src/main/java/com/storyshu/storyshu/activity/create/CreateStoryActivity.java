package com.storyshu.storyshu.activity.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.ChooseImageResultActivity;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.utils.ParcelableUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.text.RichTextEditor;
import com.storyshu.storyshu.widget.title.TitleView;

/**
 * 写故事的页面啊
 * Created by bear on 2016/12/19.
 */

public class CreateStoryActivity extends ChooseImageResultActivity {
    private static final String TAG = "CreateStoryActivity";
    private TitleView mTitleView; //标题栏
    private RichTextEditor mStoryEdit; //正文编辑栏
    private static final int IMAGE = 1;
    private static final int CAMERA = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_story_layout);
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
        mStoryEdit = (RichTextEditor) findViewById(R.id.story_edit);
        if (!TextUtils.isEmpty(ISharePreference.getContent(this)))
            mStoryEdit.setDataContent(ISharePreference.getContent(this));
        Log.i(TAG, "initView: Content:" + ISharePreference.getContent(this));
    }

    /**
     * 标题栏的设置和响应事件
     */
    private void initTitleView() {
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                ISharePreference.saveContent(CreateStoryActivity.this, mStoryEdit.getEditString());
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
                StoryInfo storyBaseInfo = new StoryInfo();
                storyBaseInfo.setContent(mStoryEdit.getEditString());
                storyBaseInfo.setExtract(mStoryEdit.getExtract());
                storyBaseInfo.setDetailPic(mStoryEdit.getCoverPic());
                ISharePreference.saveContent(CreateStoryActivity.this, mStoryEdit.getEditString());
                ISharePreference.saveCoverPic(CreateStoryActivity.this, mStoryEdit.getCoverPic());
                intentWithParcelable(CreateCoverActivity.class, ParcelableUtil.STORY, storyBaseInfo);
            }
        });

        mTitleView.setOnTitleInsertImageListener(new TitleView.OnTitleInsertImageListener() {
            @Override
            public void onInsertImageClick() {
                chooseImage();
            }
        });
    }

    @Override
    public void onResult(String imagePath) {
        //插入图片
        mStoryEdit.insertImage(imagePath);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        ISharePreference.saveExtra(CreateStoryActivity.this, mStoryEdit.getExtract());
        super.onStop();
    }
}
