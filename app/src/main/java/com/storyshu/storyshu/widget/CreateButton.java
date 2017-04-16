package com.storyshu.storyshu.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.DipPxConversion;

/**
 * 导航栏底部的加号，创建故事的按钮
 * Created by bear on 2017/3/13.
 */

public class CreateButton extends View {
    private static final String TAG = "CreateButton";
    private float mXLength; //加号的边长
    private float mXThick; //加号的厚度
    private float mSideWidth; //背景的黑色圆环宽度
    private long mAnimationTime; //动画执行时间
    private float mMaxRotateAngle; //旋转的最大角度值
    private float mRotateAngle = 0; //旋转的角度值

    private int mBgColor; //背景颜色
    private int mFgColor; //前景颜色
    private int mDownFgColor; //前景按下的颜色
    private int mUpFgColor; //前景抬起的颜色
    private int mXColor; //加号的颜色

    private Paint mPaint; //画笔
    private float mCenterX = 0; //画布中心x
    private float mCenterY = 0; //画布中心y

    private OnCreateClickListener listener;

    public void setCreateClickListener(OnCreateClickListener listener) {
        this.listener = listener;
    }

    /**
     * 按钮点击事件
     */
    public interface OnCreateClickListener {
        void onClick();
    }

    public CreateButton(Context context) {
        this(context, null);
    }

    public CreateButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CreateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mXLength = DipPxConversion.dip2px(getContext(), 10);
        mXThick = DipPxConversion.dip2px(getContext(), 2);
        mSideWidth = getResources().getDimension(R.dimen.margin_min);

        mAnimationTime = 180;
        mMaxRotateAngle = (float) (Math.PI / 4);

        mUpFgColor = getResources().getColor(R.color.colorRed);
        mDownFgColor = getResources().getColor(R.color.colorRedLight);

        mBgColor = getResources().getColor(R.color.colorWhite);
        mFgColor = mUpFgColor;
        mXColor = getResources().getColor(R.color.colorWhiteDeep);

        mPaint = new Paint();
    }

    /**
     * 绘制加号
     *
     * @param canvas
     */
    private void drawPlus(Canvas canvas) {
        double x[] = new double[4]; //端点的x坐标
        double y[] = new double[4]; //端点的y坐标

        for (int i = 0; i < 4; i++) {

            x[i] = mXLength * Math.sin(mRotateAngle + Math.PI * i / 2) + mCenterX;
            y[i] = mXLength * Math.cos(mRotateAngle + Math.PI * i / 2) + mCenterY;
        }

        canvas.drawLine((float) x[0], (float) y[0], (float) x[2], (float) y[2], mPaint);
        canvas.drawLine((float) x[1], (float) y[1], (float) x[3], (float) y[3], mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        if (mCenterX == 0) {
            mCenterX = getWidth() / 2;
            mCenterY = getHeight() / 2;
        }


        //背景圆
        mPaint.setColor(mBgColor);
        canvas.drawCircle(mCenterX, mCenterX, mCenterX, mPaint);

        //前景圆
        mPaint.setColor(mFgColor);
        canvas.drawCircle(mCenterX, mCenterY, mCenterX - (mSideWidth), mPaint);

        //加号
        mPaint.setColor(mXColor);
        mPaint.setStrokeWidth(mXThick);
        drawPlus(canvas);
    }

    /**
     * 加号动画
     *
     * @param isDown 是否是按下
     */
    private void animatePlus(boolean isDown) {
        //按着则旋转45度
        //抬起则恢复
        float end = isDown ? -mMaxRotateAngle : 0;
        clearAnimation();

        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "xRatate", mRotateAngle, end);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(mAnimationTime);
        animator.start();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotateAngle = (Float) animation.getAnimatedValue();
                invalidate(0, 0, getWidth(), getHeight());
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFgColor = mDownFgColor;
                animatePlus(true);
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mFgColor = mUpFgColor;
                animatePlus(false);

                //点击回调
                float x = event.getX();
                float y = event.getY();
                if (x < getWidth() && x > 0)
                    if (y < getHeight() && y > 0)
                        if (listener != null)
                            listener.onClick();

                break;
        }
        return true;
    }
}
