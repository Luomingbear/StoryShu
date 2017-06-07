package com.storyshu.storyshu.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.DipPxConversion;

import java.util.ArrayList;
import java.util.List;

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
    private float mButtonWidth; //按钮的宽度

    private int mBgColor; //背景颜色
    private int mFgColor; //前景颜色
    private int mDownFgColor; //前景按下的颜色
    private int mUpFgColor; //前景抬起的颜色
    private int mXColor; //加号的颜色

    private Paint mPaint; //画笔
    private float mCenterX = 0; //画布中心x
    private float mCenterY = 0; //画布中心y

    private List<StoryType> mStoryTypeList; //点击加号弹出的选项
    private boolean isShowStoryType = false; //是否启动选项动画
    private float mStoryTypeAngle = (float) (Math.PI / 5.0f * 3); //选项所在区域的角度
    private float mStoryTypeDistance; //选项所在区域与创建按钮的距离
    private float mTextSize; //选项按钮的文字大小
    private float mIconWidth; //选项按钮的宽度

    private OnCreateClickListener listener; //点击接口

    /**
     * 新建的故事类型的描述
     */
    public class StoryType {
        private String title; //图标标题
        private int drawableRes; //图标

        public StoryType(String title, int drawableRes) {
            this.title = title;
            this.drawableRes = drawableRes;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getDrawableRes() {
            return drawableRes;
        }

        public void setDrawableRes(int drawableRes) {
            this.drawableRes = drawableRes;
        }
    }

    /**
     * 设置选项是否显示
     *
     * @param showStoryType
     */
    public void setShowStoryType(boolean showStoryType) {
        isShowStoryType = showStoryType;
        postInvalidate();

        //返回显示状态
        if (listener != null) {
            if (isShowStoryType)
                listener.showingStoryType();
            else listener.dismissStoryType();
        }
    }

    public void setCreateClickListener(OnCreateClickListener listener) {
        this.listener = listener;
    }

    /**
     * 按钮点击事件
     */
    public interface OnCreateClickListener {
        void showingStoryType();

        void dismissStoryType();

        void onClick(int position);
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
        mButtonWidth = getResources().getDimension(R.dimen.navigation_height);
        mStoryTypeDistance = mButtonWidth * 1.8f;
        mTextSize = getResources().getDimension(R.dimen.font_small);
        mIconWidth = mButtonWidth / 4;

        mAnimationTime = 200;
        mMaxRotateAngle = (float) (Math.PI / 4);

        mUpFgColor = getResources().getColor(R.color.colorRed);
        mDownFgColor = getResources().getColor(R.color.colorRedLight);

        mBgColor = getResources().getColor(R.color.colorWhite);
        mFgColor = mUpFgColor;
        mXColor = getResources().getColor(R.color.colorWhiteDeep);

        mPaint = new Paint();

        mStoryTypeList = new ArrayList<>();
        initStoryTypeList();
    }

    /**
     * 初始化需要弹出的类型
     */
    private void initStoryTypeList() {
        StoryType storyType1 = new StoryType(getResources().getString(R.string.long_story), R.drawable.long_story);
        StoryType storyType2 = new StoryType(getResources().getString(R.string.mic_story), R.drawable.mic_story);
        StoryType storyType3 = new StoryType(getResources().getString(R.string.video), R.drawable.video);

        mStoryTypeList.add(storyType1);
        mStoryTypeList.add(storyType2);
        mStoryTypeList.add(storyType3);
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
        mPaint.setAntiAlias(true); //
        mPaint.setFilterBitmap(true); //
        mPaint.setStyle(Paint.Style.FILL);

        if (mCenterX == 0) {
            mCenterX = getWidth() / 2;
            mCenterY = getHeight() - mButtonWidth / 2;
        }


        //背景圆
        mPaint.setColor(mBgColor);
        canvas.drawCircle(mCenterX, mCenterX, mButtonWidth / 2, mPaint);

        //前景圆
        mPaint.setColor(mFgColor);
        canvas.drawCircle(mCenterX, mCenterY, mButtonWidth / 2 - (mSideWidth), mPaint);

        //加号
        mPaint.setColor(mXColor);
        mPaint.setStrokeWidth(mXThick);
        drawPlus(canvas);

        //选项
        if (isShowStoryType) {
            drawStoryTypeList(canvas);
        }
    }

    /**
     * 绘制选项
     *
     * @param canvas
     */
    private void drawStoryTypeList(Canvas canvas) {
        if (mStoryTypeList.size() == 0)
            return;

        for (int i = 0; i < mStoryTypeList.size(); i++) {
            drawStoryType(canvas, mStoryTypeList.get(i), i);
        }
    }

    /**
     * 绘制选项
     *
     * @param canvas
     */
    private void drawStoryType(Canvas canvas, StoryType storyType, int index) {
        double angle = (Math.PI + mStoryTypeAngle) / 2 - (mStoryTypeAngle / (mStoryTypeList.size() - 1) * index) + mRotateAngle;
        double x = mCenterX + Math.cos(angle) * mStoryTypeDistance;
        double y = mCenterY - Math.sin(angle) * mStoryTypeDistance;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), storyType.drawableRes);
        Rect rectDes = new Rect((int) (x - mIconWidth / 2), (int) (y - mIconWidth / 2),
                (int) (x + mIconWidth / 2), (int) (y + mIconWidth / 2));
        Rect rectSrc = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        //
        mPaint.setColor(mFgColor);
        canvas.drawCircle((float) x, (float) y + mIconWidth / 2, mButtonWidth / 2 - mSideWidth, mPaint);
        //
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mSideWidth / 2);
        mPaint.setColor(mBgColor);
        canvas.drawCircle((float) x, (float) y + mIconWidth / 2, mButtonWidth / 2 - mSideWidth, mPaint);

        //图标
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(bitmap, rectSrc, rectDes, mPaint);
        //
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mTextSize);
        float width = mPaint.measureText(storyType.getTitle());
        canvas.drawText(storyType.getTitle(), (float) x - width / 2, (float) y + mIconWidth / 2 + mSideWidth + mTextSize, mPaint);
    }

    /**
     * 加号动画
     *
     * @param isDown 是否是按下
     */
    private void animatePlus(boolean isDown) {
        Log.i(TAG, "animatePlus: " + isDown);
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                //如果点击的地方是选项
                if (isShowStoryType) {
                    for (int i = 0; i < mStoryTypeList.size(); i++) {
                        double angle = (Math.PI + mStoryTypeAngle) / 2 - (mStoryTypeAngle / (mStoryTypeList.size() - 1) * i) + mRotateAngle;
                        double x = mCenterX + Math.cos(angle) * mStoryTypeDistance;
                        double y = mCenterY - Math.sin(angle) * mStoryTypeDistance + mIconWidth / 2;
                        if (event.getX() > x - mButtonWidth / 2 && event.getX() < x + mButtonWidth / 2) {
                            if (event.getY() > y - mButtonWidth / 2 && event.getY() < y + mButtonWidth / 2) {
                                return true;
                            }
                        }
                    }
                } else {
                    /**
                     * 点击创建按钮则显示选项动画
                     * 点击选项则返回点击的位置
                     * 否则不显示选项
                     */
                    if (event.getX() < mCenterX + mButtonWidth / 2 && event.getX() > mCenterX - mButtonWidth / 2) {
                        if (event.getY() < mCenterY + mButtonWidth / 2 && event.getY() > mCenterY - mButtonWidth / 2) {
                            mFgColor = mDownFgColor;
                            animatePlus(true);
                            return true;
                        }
                    }
                }

                //点击了其他的地方
                isShowStoryType = false;
                postInvalidate(); //刷新

                if (listener != null)
                    listener.dismissStoryType();

                return false;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                mFgColor = mUpFgColor;
                animatePlus(false);

                if (event.getX() < mCenterX + mButtonWidth / 2 && event.getX() > mCenterX - mButtonWidth / 2) {
                    if (event.getY() < mCenterY + mButtonWidth / 2 && event.getY() > mCenterY - mButtonWidth / 2) {
                        isShowStoryType = true;
                    }
                }

                //如果点击的地方是选项
                for (int i = 0; i < mStoryTypeList.size(); i++) {
                    double angle = (Math.PI + mStoryTypeAngle) / 2 - (mStoryTypeAngle / (mStoryTypeList.size() - 1) * i) + mRotateAngle;
                    double x = mCenterX + Math.cos(angle) * mStoryTypeDistance;
                    double y = mCenterY - Math.sin(angle) * mStoryTypeDistance + mIconWidth / 2;
                    if (event.getX() > x - mButtonWidth / 2 && event.getX() < x + mButtonWidth / 2) {
                        if (event.getY() > y - mButtonWidth / 2 && event.getY() < y + mButtonWidth / 2) {
                            mFgColor = mUpFgColor;
                            isShowStoryType = false;
                            postInvalidate();

                            if (listener != null) {
                                listener.onClick(i);
                            }
                        }
                    }
                }


                if (isShowStoryType) {
                    if (listener != null)
                        listener.showingStoryType();
                } else {
                    if (listener != null)
                        listener.dismissStoryType();
                }
                return true;

        }

        return super.dispatchTouchEvent(event);
    }
}
