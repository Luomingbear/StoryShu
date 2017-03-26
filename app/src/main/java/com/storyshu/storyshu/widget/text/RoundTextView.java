package com.storyshu.storyshu.widget.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.storyshu.storyshu.R;

/**
 * 带圆角的文本显示组件
 * Created by bear on 2017/2/6.
 */

public class RoundTextView extends AppCompatTextView {
    private Paint mPaint;
    private int mBgColor; //背景颜色
    private float mRoundSize; //圆角的值
    private RoundType mRoundType = RoundType.ROUND_RECT; //圆角的类型

    public enum RoundType {
        ROUND_RECT,

        ROUND_TOP,

        ROUND_BOTTOM
    }

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
        mRoundType = RoundType.values()[typedArray.getInt(R.styleable.RoundTextView_roundType, 0)];
        typedArray.recycle();
        init();
    }

    public int getBgColor() {
        return mBgColor;
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

    public float getRoundSize() {
        return mRoundSize;
    }

    public void setRoundSize(float roundSize) {
        this.mRoundSize = roundSize;
        postInvalidate();
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
        switch (mRoundType) {
            case ROUND_RECT:
                break;
            case ROUND_TOP:
                canvas.drawRect(0, getHeight() - mRoundSize, mRoundSize, getHeight(), mPaint);
                canvas.drawRect(getWidth() - mRoundSize, getHeight() - mRoundSize, getWidth(), getHeight(), mPaint);
                break;
            case ROUND_BOTTOM:
                canvas.drawRect(0, 0, mRoundSize, mRoundSize, mPaint);
                canvas.drawRect(getWidth() - mRoundSize, 0, getWidth(), mRoundSize, mPaint);
                break;
        }
        super.onDraw(canvas);
    }
}
