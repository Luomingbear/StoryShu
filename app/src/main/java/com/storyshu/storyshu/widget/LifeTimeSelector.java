package com.storyshu.storyshu.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.storyshu.storyshu.R;

import java.util.ArrayList;

/**
 * 选择故事的停留时间
 * Created by bear on 2017/3/27.
 */

public class LifeTimeSelector extends View {
    private static final String TAG = "LifeTimeSelector";
    private float mDefTextSize; //默认的文字大小
    private float mHitTextSize; //选择的文字大小
    private int mDefTextColor; //默认的文字颜色
    private int mHitTextColor; //选择的文字颜色

    private float mLeftTextX; //左边的文字的中心x左边
    private float mRightTextX; //右边的文字的中心x左边
    private float mItemHeight; //每一个条的高度
    private float mCenterHeight; //中间高度
    private Paint mPaint;

    private ArrayList<Integer> mDayList; //天数的数值
    private ArrayList<Integer> mHourList; //小时的数值
    private ArrayList<String> mUnitList; //单位的值
    private int mDayIndex = 0; //当前选择的下标
    private int mHourIndex = 0; //当前选择的下标
    private int mUnitIndex = 0; //当前选择的下标
    private float mNumY = 0; //数值列表的第一个元素的y坐标
    private float mUnitY = 0; //单位列表的第一个元素的y坐标
    private int mCatchNum = 7; //数值的缓存树目

    private ObjectAnimator anim; //动画
    private int animateTime = 400; //动画时间

    /**
     * 获取生命期
     *
     * @return 单位分钟
     */
    public int getLifeTime() {
        if (mUnitIndex == 0)
            return mDayList.get(mDayIndex) * 24 * 60;
        else return mHourList.get(mHourIndex) * 60;
    }

    public LifeTimeSelector(Context context) {
        this(context, null);
    }

    public LifeTimeSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LifeTimeSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDefTextSize = getResources().getDimension(R.dimen.font_big);
        mHitTextSize = getResources().getDimension(R.dimen.font_big);

        mHitTextColor = getResources().getColor(R.color.colorRedLight);
        mDefTextColor = getResources().getColor(R.color.colorGray);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        //初始化单位和数值
        mDayList = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            mDayList.add(i);
        }

        mHourList = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            mHourList.add(i);
        }

        mUnitList = new ArrayList<>();
        mUnitList.add(getResources().getString(R.string.day_unit));
        mUnitList.add(getResources().getString(R.string.hour_unit));
    }

    /**
     * 画中间的框
     *
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(0, mItemHeight, getWidth(), mItemHeight * 2, mPaint);
        mPaint.setColor(mDefTextColor);
        canvas.drawRect(0, mItemHeight, getWidth(), mItemHeight + 2, mPaint);
        canvas.drawRect(0, mItemHeight * 2, getWidth(), mItemHeight * 2 + 2, mPaint);
    }

    /**
     * 绘制文本
     *
     * @param canvas 画布
     * @param text   文字内容
     * @param x      x坐标
     * @param y
     * @param size   字体大小
     * @param color  文字颜色
     */
    private void drawText(Canvas canvas, String text, float x, float y, float size, int color) {
        mPaint.setColor(color);
        mPaint.setTextSize(size);

        canvas.drawText(text, x - mPaint.measureText(text) / 2.0f, y + size / 3, mPaint);
    }

    /**
     * 画单位
     * 天／小时
     *
     * @param canvas
     */
    private void drawUnit(Canvas canvas) {
        //临时的下标值，标志被选中的下标
        int num = 3;
        int[] index = new int[num];
        //如果下标小于0，则返回最后一个
        index[0] = mUnitIndex - 1 < 0 ? mUnitList.size() - 1 : mUnitIndex - 1;//上面的一个值
        //
        index[1] = mUnitIndex;
        //如果下标大于等于列表的size，则返回第一个
        index[2] = mUnitIndex + 1 >= mUnitList.size() ? 0 : mUnitIndex + 1;
        int color;

        for (int i = 0; i < num; i++) {
            //显示的高度
            float y = mUnitY + mItemHeight / 2 + mItemHeight * i;
            //只有选择的颜色高亮
            if (y > mItemHeight && y <= mItemHeight * 2)
                color = mHitTextColor;
            else color = mDefTextColor;

            //绘制文本
            drawText(canvas, mUnitList.get(index[i]), mRightTextX, y, mDefTextSize, color);
        }
    }

    /**
     * 画数值
     *
     * @param canvas
     */
    private void drawNum(Canvas canvas) {
        //临时的列表
        ArrayList<Integer> list;
        //临时的下标值，标志被选中的下标
        int center;

        /**
         * 如果当前的单位是天，则使用dayList
         * 否则使用hourList
         */
        if (mUnitIndex == 0) {
            list = mDayList;
            center = mDayIndex;
        } else {
            list = mHourList;
            center = mHourIndex;
        }

        int[] index = new int[mCatchNum];
        for (int i = 0; i < mCatchNum; i++) {
            //在选择的上方
            if (i < mCatchNum / 2) {
                //临时下标
                index[i] = center - mCatchNum / 2 + i;
                //真实下标
                index[i] = index[i] < 0 ? list.size() + index[i] : index[i];
            } else if (i == mCatchNum / 2) {
                index[i] = center;
            } else {
                //在选择的下方
                int j = center + i - mCatchNum / 2;
                if (j >= list.size())
                    index[i] = j - list.size();
                else index[i] = j;
            }
        }

        for (int i = 0; i < mCatchNum; i++) {
            //显示的高度
            float y = mNumY + mItemHeight / 2 + mItemHeight * i;

            //只有选择的颜色高亮
            int color;
            if (y > mItemHeight && y <= mItemHeight * 2)
                color = mHitTextColor;
            else color = mDefTextColor;

            //绘制文本
            drawText(canvas, list.get(index[i]) + "", mLeftTextX, y, mDefTextSize, color);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mLeftTextX = getWidth() / 4;
        mRightTextX = getWidth() / 4 * 3;
        mItemHeight = getHeight() / 3;
        mCenterHeight = getHeight() / 2;

        mNumY = mItemHeight * (1 - mCatchNum / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制中心的选中条背景
        drawRect(canvas);

        //绘制文字单位
        drawUnit(canvas);

        //绘制数值
        drawNum(canvas);
    }

    /**
     * 更新下标
     *
     * @param moveY 显示的y坐标
     * @param downX 手指按下的x坐标
     */
    private void changeIndex(float moveY, float downX) {
        /**
         * 手指按下在左边则是滑动的数值
         * 在右边是滑动的单位
         */
        if (downX < getWidth() / 2) {
            int center = mUnitIndex == 0 ? mDayIndex : mHourIndex;
            ArrayList<Integer> list = mUnitIndex == 0 ? mDayList : mHourList;
            if (mNumY + mItemHeight / 2 + mItemHeight * (mCatchNum / 2) < mItemHeight) {
                int change = (int) ((mItemHeight / 2 * 3 - (mNumY + mItemHeight / 2 + mItemHeight * (mCatchNum / 2))) / mItemHeight);
                center += change;
                center = center >= list.size() ? 0 : center;
            } else if (mNumY + mItemHeight / 2 + mItemHeight * (mCatchNum / 2) > mItemHeight * 2) {
                int change = (int) ((mNumY + mItemHeight / 2 + mItemHeight * (mCatchNum / 2) - mItemHeight / 2 * 3) / mItemHeight);
                center -= change;
                center = center < 0 ? list.size() - 1 : center;
            }

            if (mUnitIndex == 0) {
                mDayIndex = center;
            } else mHourIndex = center;
        } else {
            //单位
            if (mUnitY + mItemHeight / 2 + mItemHeight * 1 < mItemHeight) {
                mUnitIndex++;
                mUnitIndex = mUnitIndex >= mUnitList.size() ? 0 : mUnitIndex;
            } else if (mUnitY + mItemHeight / 2 + mItemHeight * 1 > mItemHeight * 2) {
                mUnitIndex--;
                mUnitIndex = mUnitIndex < 0 ? mUnitList.size() - 1 : mUnitIndex;
            }
            if (mUnitIndex == 0)
                mDayIndex = 0;
        }
    }

    /**
     * 移动
     *
     * @param moveY 移动的y坐标
     * @param downX 手指按下的x坐标 判断移动数值还是单位
     */
    private void move(float moveY, float downX) {
        /**
         * 如果按下的时候在坐标，则移动数值
         * 否则移动单位
         */
        if (downX < getWidth() / 2) {
            mNumY += moveY;
        } else {
            mUnitY += moveY;
        }

        postInvalidate();
    }

    /**
     * 自动滚动
     *
     * @param acceleration 手指滑动的加速度
     * @param upY          手指抬起的y坐标
     */
    private void animateMove(float acceleration, float upY) {
        /**
         * 动画
         */
        if (anim != null) {
            anim.cancel();
            anim = null;
        }

        float startY, endY;
        if (downX < getWidth() / 2) {
            startY = mNumY;
            endY = mItemHeight * (1 - mCatchNum / 2);
        } else {
            startY = mUnitY;
            endY = 0;
        }

        double time = Math.pow(0.8, acceleration) * animateTime;
        anim = ObjectAnimator//
                .ofFloat(this, "oxo", startY, endY)// 自己随便写的数据名，我们的目的是获取插值
                .setDuration((long) time);//
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (downX < getWidth() / 2) {
                    mNumY = (Float) animation.getAnimatedValue();
                } else {
                    mUnitY = (Float) animation.getAnimatedValue();
                }
                postInvalidate();
            }
        });
    }

    /**
     * 手指触摸事件
     */
    private float downX, downY; //手指按下的坐标
    private float moveY; //手指上一次移动到的y坐标
    private long downTime; //手指按下的时间

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                moveY = downY;
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                move(y - moveY, downX);
                moveY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float upY = event.getY();
                changeIndex(upY - downY, downX);
                //自动滚动
                long moveTime = System.currentTimeMillis() - downTime;
                float acc = (upY - downY) / moveTime;
                animateMove(acc, upY);
                break;
        }
        return true;
    }
}