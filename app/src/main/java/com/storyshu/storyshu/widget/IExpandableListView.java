package com.storyshu.storyshu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 修复了嵌套在scrollView里的高度问题
 * Created by bear on 2017/3/21.
 */

public class IExpandableListView extends ExpandableListView {
    public IExpandableListView(Context context) {
        super(context);
    }

    public IExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        //将重新计算的高度传递回去
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
