package com.storyshu.storyshu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.adapter.card.CardViewAdapter;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户的简介界面
 * Created by bear on 2017/6/5.
 */

public class UserIntroductionActivity extends IBaseActivity {
    private DiscreteScrollView mRecyclerView;
    private List<CardInfo> mList; //动态数据
    private CardViewAdapter mCardViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_introduction_layout);

        initView();
        initEvent();
        initData();
    }

    private void initView() {
        StatusBarUtils.setColor(UserIntroductionActivity.this, R.color.colorRed);
        //
        mRecyclerView = (DiscreteScrollView) findViewById(R.id.card_list);
    }

    private void initEvent() {
        mList = new ArrayList<>();
        mCardViewAdapter = new CardViewAdapter(mList, UserIntroductionActivity.this);

        mRecyclerView.setAdapter(mCardViewAdapter);
        mRecyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.9f)
                .build()
        );
    }

    private void initData() {
        for (int i = 0; i < 3; i++) {
            CardInfo cardInfo = new CardInfo();
            cardInfo.setLikeNum(22);
            mList.add(cardInfo);
        }

        mCardViewAdapter.notifyDataSetChanged();
    }
}
