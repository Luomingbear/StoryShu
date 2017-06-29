package com.storyshu.storyshu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.tool.observable.EventObserver;

/**
 * 直线的进度条
 * Created by bear on 2017/5/14.
 */

public class LineProgressBar extends View implements EventObserver {
    private int mBgColor; //背景颜色
    private int mFgColor; //线条颜色

    private int progress = 0; //进度

    private Paint mPaint;

    public LineProgressBar(Context context) {
        this(context, null);
    }

    public LineProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public LineProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineProgressBar);
        mBgColor = typedArray.getColor(R.styleable.LineProgressBar_progressBgColor, getResources().getColor(R.color.colorGrayLight));
        mFgColor = typedArray.getColor(R.styleable.LineProgressBar_progressFgColor, getResources().getColor(R.color.colorRedLight));
        typedArray.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaint.setStyle(Paint.Style.FILL);
    }

    private void drawRectLine(Canvas canvas, int progress, int color) {
        RectF rectF = new RectF(0, 0, progress / 100.0f * getWidth(), getHeight());
        mPaint.setColor(color);
        canvas.drawRect(rectF, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (progress != 0 && progress != 100) {
            //画背景

            drawRectLine(canvas, 100, mBgColor);

            //画前景
            drawRectLine(canvas, progress, mFgColor);
        }
    }

    public void setmBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
    }

    public void setmFgColor(int mFgColor) {
        this.mFgColor = mFgColor;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public void onNotify(Object sender, int eventId, Object... args) {
        if (eventId == R.id.line_progress_bar) {
            setProgress((Integer) args[0]);
        }
    }
}
