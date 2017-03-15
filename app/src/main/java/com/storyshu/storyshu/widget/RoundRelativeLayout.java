package com.storyshu.storyshu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.storyshu.storyshu.R;

/**
 * 带有圆角背景的布局
 * Created by bear on 2017/3/15.
 */

public class RoundRelativeLayout extends RelativeLayout {
    private int mBorderColor; //边框的颜色
    private float mBorderWidth; //边框的宽度
    private float mBorderRadius; //圆角的半径
    private int mBgColor;
    private Type mType; //圆角的类型
    private Paint mPaint;

    public enum Type {
        /**
         * 四个角都是圆角
         */
        Round,

        /**
         * 上面的两个角是圆角
         */
        TopRound,

        /**
         * 下面的两个角是圆角
         */
        BottomRound
    }

    public RoundRelativeLayout(Context context) {
        this(context, null);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundRelativeLayout);
        mBorderColor = typedArray.getColor(R.styleable.RoundRelativeLayout_rBorderColor, getResources().getColor(R.color.colorGrayLight));
        mBorderRadius = typedArray.getDimension(R.styleable.RoundImageView_BorderRadius, getResources().getDimension(R.dimen.border_radius_normal));
        mBorderWidth = typedArray.getDimension(R.styleable.RoundRelativeLayout_rBorderWidth, 2);
        mBgColor = typedArray.getColor(R.styleable.RoundRelativeLayout_rBackground, getResources().getColor(R.color.colorWhite));
        mType = Type.values()[typedArray.getInt(R.styleable.RoundRelativeLayout_rType, 0)];

        typedArray.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    /**
     * 绘制圆角矩形
     *
     * @param canvas
     */
    private void drawRoundRect(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBorderWidth);

        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rectF, mBorderRadius, mBorderRadius, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBgColor);
        canvas.drawRoundRect(rectF, mBorderRadius, mBorderRadius, mPaint);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mType) {
            case Round:
                drawRoundRect(canvas);
                break;
            case TopRound:

                break;
            case BottomRound:

                break;
        }

    }
}
