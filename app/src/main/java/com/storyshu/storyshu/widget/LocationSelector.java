package com.storyshu.storyshu.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.amap.api.services.core.PoiItem;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.DipPxConversion;

import java.util.ArrayList;
import java.util.List;

/**
 * 位置选择器，类似时间选择器
 * setLocationList() 填充位置数据列表
 * getSelectedPoi() 获取选择的地点
 * <p>
 * Created by bear on 2017/2/15.
 */

public class LocationSelector extends View {
    private static final String TAG = "LocationSelector";
    private Paint mPaint;
    private int mSelectedColor; //选中的条目的颜色
    private int mNormalColor; //普通的条目的颜色
    private float mSelectedFontSize; //选中的条目的字体大小
    private float mNormalFontSize; //普通的条目的字体大小

    private float mHeadY = 0; //第一个条目的y位置
    private float mSelectRectX, mSelectRectY, mSelectRectW, mSelectRectH; //显示选中的方框的位置
    private float mStorkeWidth;

    private List<PoiItem> mLocationList; //地点数据集
    private List<String> mShowTextList; //地点数据集
    private int selectedIndex = 0; //选择的条目的下标
    private int maxShowCount = 4; //同时显示的最大条目
    private float ratio; //高宽比例


    public LocationSelector(Context context) {
        this(context, null);
    }

    public LocationSelector(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocationSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LocationSelector);
        ratio = typedArray.getFloat(R.styleable.LocationSelector_selectRatio, 1.0f);
        typedArray.recycle();
        init();
    }

    /**
     * 得到地点数据
     *
     * @return
     */
    public List<PoiItem> getLocationList() {
        return mLocationList;
    }

    /**
     * 设置地点数据
     *
     * @param locationList
     */
    public void setLocationList(List<PoiItem> locationList) {
        this.mLocationList = locationList;
        if (locationList == null)
            return;
        initShowList();
        postInvalidate();
    }

    /**
     * 获取当前选择的地点
     *
     * @return
     */
    public PoiItem getSelectedPoi() {
        return mLocationList == null ? null : mLocationList.get(selectedIndex);
    }

    /**
     * 初始化
     */
    private void init() {
        mSelectedColor = getResources().getColor(R.color.colorRedLight);
        mNormalColor = getResources().getColor(R.color.colorGrayLight);

        mSelectedFontSize = getResources().getDimension(R.dimen.font_normal);
        mNormalFontSize = getResources().getDimension(R.dimen.font_small);

        mStorkeWidth = DipPxConversion.dip2px(getContext(), 2);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mLocationList = new ArrayList<>();
        mShowTextList = new ArrayList<>();
    }

    private void initShowList() {
        mPaint.setTextSize(mNormalFontSize);
        mShowTextList.clear();
        if (mLocationList == null)
            return;
        for (PoiItem poi : mLocationList) {
            String location = getShowText(poi.getTitle(), mPaint);
            if (!TextUtils.isEmpty(location))
                mShowTextList.add(location);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        mSelectRectX = mStorkeWidth / 2;
        mSelectRectY = h / maxShowCount;
        mSelectRectW = w - mStorkeWidth / 2;
        mSelectRectH = h / maxShowCount;

        initShowList();
    }

    /**
     * 绘制渐变的背景
     *
     * @param canvas
     */
    private void drawBg(Canvas canvas) {
        LinearGradient lg = new LinearGradient(getWidth() / 2, mSelectRectY + mSelectRectH, getWidth() / 2, getHeight(),
                Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(lg);

        canvas.drawRect(mStorkeWidth, 0, getWidth() - mStorkeWidth, getHeight(), mPaint);
    }

    /**
     * 绘制选择的方块
     *
     * @param canvas
     */
    private void drawSelectRect(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mNormalColor);

        mPaint.setStrokeWidth(mStorkeWidth);
        RectF rectF = new RectF(mSelectRectX, mSelectRectY, mSelectRectW, mSelectRectY + mSelectRectH);
        canvas.drawRect(rectF, mPaint);

    }

    /**
     * 获取应该显示的文本内容
     * <p>
     * 显示不全会省略前面的文本
     *
     * @param s
     * @param paint
     * @return
     */
    private String getShowText(String s, Paint paint) {
        String showText = s;
        float textWidth = paint.measureText(s);
        float dosWidth = mPaint.measureText("...");
        if (textWidth <= mSelectRectW - mNormalFontSize * 2)
            return s;
        else {
            for (int i = s.length() - 1; i >= 0; i--) {
                textWidth = mPaint.measureText(s, i, s.length() - 1) + dosWidth;
                if (textWidth >= mSelectRectW - mNormalFontSize * 2) {
                    showText = "..." + s.substring(i + 1);
                    return showText;
                }
            }
        }

        return showText;
    }

    /**
     * 绘制地点文本列表
     *
     * @param canvas
     */
    private void drawLocationText(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);

        //渐变
//        LinearGradient lg = new LinearGradient(getWidth() / 2, mSelectRectY + mSelectRectH / 4, getWidth() / 2, mSelectRectH * 2,
//                mSelectedColor, mNormalColor, Shader.TileMode.CLAMP);
//        mPaint.setShader(lg);

        /**
         * 循环添加文本
         */
        mPaint.setTextSize(mNormalFontSize);

        for (int i = 0; i < mShowTextList.size(); i++) {
            //mHeadY是为了实现动画效果而加人的变量，
            //文本显示的y坐标就是 ：选择框的高度 x 文本在列表的下标 - 文字显示的高度的1.3倍
            //1.3倍是为了让文字始终在选择框的中心
            float textY = mHeadY + mSelectRectH * (i + 2) - mNormalFontSize * 1.3f;
            if (textY > mSelectRectY && textY < 2 * mSelectRectY) {
//                mPaint.setTextSize(mSelectedFontSize);
                mPaint.setColor(mSelectedColor);
                mPaint.setAlpha(255);
                //保存当前的选择
                selectedIndex = i;
//                Log.i(TAG, "drawLocationText: 选择的地点是：" + mLocationList.get(i));

            } else {
                mPaint.setColor(mNormalColor);
                mPaint.setAlpha((int) (255 - (Math.abs(textY - mSelectRectY)) / mSelectRectH / 2 * 30));

            }
            canvas.drawText(mShowTextList.get(i), mNormalFontSize, textY, mPaint);
        }
    }

    /**
     * 绘制高亮的地点
     *
     * @param canvas
     */
    private void drawSelectText(Canvas canvas) {
        mPaint.setColor(mSelectedColor);
        RectF rectF = new RectF(mSelectRectX, mSelectRectY, mSelectRectW, mSelectRectY + mSelectRectH);
        canvas.clipRect(rectF);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLocationList == null)
            mHeadY = 0;
        else {
            mHeadY = Math.min(0, mHeadY);
            mHeadY = Math.max(-mSelectRectH * (mLocationList.size() - 1), mHeadY);
        }
//        drawBg(canvas);

        drawLocationText(canvas);

        mPaint.setShader(null);

        drawSelectRect(canvas);


    }

    /**
     * 是否自动向上滑动
     *
     * @return true:向上
     */
    private boolean isUp() {

        float y = mHeadY % mSelectRectH;
        if (y < mSelectRectH / 2)
            return true;
        else return false;
    }

    private ObjectAnimator anim;

    /**
     * 手指抬起时的自动回弹效果
     */
    private void autoMove() {
        float endY = (mHeadY) % mSelectRectH;
        if (isUp()) {
            endY = -endY;
        } else {
            endY = mSelectRectH - endY;
        }

        //定时刷新view，形成自动回弹的效果
        int spendTime = 180; //动画执行的时间
        float targetY = mHeadY + endY;

        /**
         * 动画
         */
        if (anim != null) {
            anim.cancel();
            anim = null;
        }

        anim = ObjectAnimator//
                .ofFloat(this, "autoMove", mHeadY, targetY)// 自己随便写的数据名，我们的目的是获取插值
                .setDuration(spendTime);//
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mHeadY = (Float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

    }

    private float downX, downY; //手指按下的坐标
    private float moveX, moveY; //手指上次移动的坐标

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();

                moveX = downX;
                moveY = downY;
                break;

            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                float distance = y - moveY;
                mHeadY += distance;
                moveY = y;
                postInvalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                autoMove();
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (ratio > 0) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
            // Children are just made to fill our space.
            int childWidthSize = getMeasuredWidth();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * ratio), MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
