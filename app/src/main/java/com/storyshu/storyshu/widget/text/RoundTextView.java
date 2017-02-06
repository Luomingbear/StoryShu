package com.storyshu.storyshu.widget.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.storyshu.storyshu.R;

/**
 * 带圆角的文本显示组件
 * Created by bear on 2017/2/6.
 */

public class RoundTextView extends TextView {
    private Paint mPaint;
    private int mBgColor; //背景颜色
    private float mRoundSize; //圆角的值

    public RoundTextView(Context context) {
        this(context, null);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView);
        mRoundSize = typedArray.getDimension(R.styleable.RoundTextView_roundSize, getResources().getDimension(R.dimen.border_radius_normal));
        mBgColor = typedArray.getColor(R.styleable.RoundTextView_bgColor, getResources().getColor(R.color.colorGrayLight));
        typedArray.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mBgColor);
        mPaint.setAntiAlias(true);
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rectF, mRoundSize, mRoundSize, mPaint);
        super.onDraw(canvas);
    }

    /**
     * 设置textView的背景色
     *
     * @param BgColorRes
     */
    public void setBgColor(int BgColorRes) {
        this.mBgColor = getResources().getColor(BgColorRes);
        invalidate();
    }
}
