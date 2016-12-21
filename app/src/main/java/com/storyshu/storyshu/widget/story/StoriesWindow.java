package com.storyshu.storyshu.widget.story;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;

import com.storyshu.storyshu.R;

/**
 * 故事集的Dialog
 * Created by bear on 2016/12/4.
 */

public class StoriesWindow extends PopupWindow {
    private Context mContext;

    public StoriesWindow(Context context) {
        this(context, null);
    }

    public StoriesWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoriesWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void init(Window window) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.stories_dialog_layout, null);
        setContentView(view);

        //是否可以点击
        setTouchable(true);
        //点击外部消失
//        setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        setBackgroundDrawable(new BitmapDrawable());

        //设置背景颜色
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.dimAmount = 0.7f; //0.0-1.0
//        window.setAttributes(lp);

        //设置宽度和高度；
        Display display = window.getWindowManager().getDefaultDisplay();
        float height = display.getHeight() * 0.6f;
        setWidth(display.getWidth());
        setHeight((int) height);
    }
}
