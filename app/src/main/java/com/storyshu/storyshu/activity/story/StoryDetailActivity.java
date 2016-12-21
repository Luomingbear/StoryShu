package com.storyshu.storyshu.activity.story;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.widget.title.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 故事详情
 * Created by bear on 2016/12/21.
 */

public class StoryDetailActivity extends IBaseActivity {
    private CardInfo mCardInfo; //卡片信息
    private TitleView mTitleView; //标题栏

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_detail_layout);

        init();

        initView();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mCardInfo = getIntent().getParcelableExtra("story");
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTitle();

        //封面图
        ImageView detailPic = (ImageView) findViewById(R.id.story_detail_pic);
        ImageLoader.getInstance().displayImage(mCardInfo.getDetailPic(), detailPic);


    }


    /**
     * 初始化标题栏
     */
    private void initTitle() {
        //标题
        mTitleView = (TitleView) findViewById(R.id.title_view);
//        mTitleView.setTitleString(mCardInfo.getTitle());


        //按钮
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

            }
        });

    }
}
