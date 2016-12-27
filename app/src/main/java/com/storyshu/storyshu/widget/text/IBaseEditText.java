package com.storyshu.storyshu.widget.text;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 自定义的EditText
 * 添加了
 * Created by bear on 2016/12/24.
 */

public class IBaseEditText extends EditText {
    public IBaseEditText(Context context) {
        super(context);
        init();
    }

    public IBaseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IBaseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundResource(0);
    }
}
