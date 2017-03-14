package com.storyshu.storyshu.adapter.card;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * 故事卡片的viewpage适配器
 * Created by bear on 2017/3/14.
 */

public class StoryPageAdapter extends PagerAdapter {
    private ArrayList<?> mStoryList; //故事的数据集合

    public StoryPageAdapter() {
    }

    @Override
    public int getCount() {
        return mStoryList == null ? 0 : mStoryList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
