package com.storyshu.storyshu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 聊天的气泡控件
 * Created by bear on 2017/5/26.
 */

public class MessageTextView extends AppCompatTextView {
    private Paint mPaint;
    private MessageType messageType = MessageType.ME; //气泡类型,默认是用户本人

    public enum MessageType {
        //自己
        ME,

        //别人
        OTHER
    }

    public MessageTextView(Context context) {
        this(context, null);
    }

    public MessageTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    private void drawBg(Canvas canvas) {

    }

    @Override
    protected void onDraw(Canvas canvas) {

        /**
         * 绘制背景
         */
        drawBg(canvas);

        /**
         * 绘制文字等
         */
        super.onDraw(canvas);
    }
}
