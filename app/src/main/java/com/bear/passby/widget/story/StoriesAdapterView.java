package com.bear.passby.widget.story;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * 自定义的卡片滑动控件，根据数据自动生成卡片布局
 * Created by bear on 2016/12/4.
 */

public class StoriesAdapterView extends AdapterView {
    public StoriesAdapterView(Context context) {
        super(context);
    }

    public StoriesAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StoriesAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Adapter getAdapter() {
        return null;
    }

    @Override
    public void setAdapter(Adapter adapter) {

    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int position) {

    }
}
