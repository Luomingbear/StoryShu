package com.storyshu.storyshu.widget.text;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 基本的文本控件
 * Created by bear on 2016/12/24.
 */

public class IBaseTextView extends AppCompatTextView {
    private int position = 0;

    public IBaseTextView(Context context) {
        super(context);
        init();
    }

    public IBaseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IBaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
