package com.storyshu.storyshu.widget.text;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 基本的文本控件
 * Created by bear on 2016/12/24.
 */

public class IBaseTextView extends TextView {
    public IBaseTextView(Context context) {
        super(context);
    }

    public IBaseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IBaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
