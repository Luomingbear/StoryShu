package com.storyshu.storyshu.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.storyshu.storyshu.R;

/**
 * 侧滑布局
 * Created by bear on 2017/1/26.
 */

public class SideSlipLayout extends FrameLayout {
    private static final String TAG = "SideSlipLayout";
    private View mHomeLayout; //主界面
    private View mSideLayout; //侧边界面
    private int mHomeLayoutRes; //主界面的layout
    private int mSideLayoutRes;  //侧边的layout

    private int mLayoutWidth; //布局的宽度
    private int mSideWidth; //侧边栏的宽度

    private boolean isShowingSide = false; //侧边栏是否正显示

    private int mAnimateTime = 280; //动画执行时间 毫秒
    private float mHomeLayoutScaleRatio = 0.8f; //显示侧边栏的时候主界面缩放的目标大小

    public SideSlipLayout(Context context) {
        this(context, null);
    }

    public SideSlipLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideSlipLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SideSlipLayout);
        mHomeLayoutRes = typedArray.getResourceId(R.styleable.SideSlipLayout_homeLayout, R.layout.activity_story_map_layout);
        mSideLayoutRes = typedArray.getResourceId(R.styleable.SideSlipLayout_SideLayout, R.layout.activity_menu_layout);
        typedArray.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        //
        mHomeLayout = LayoutInflater.from(getContext()).inflate(mHomeLayoutRes, null);

        //
        mSideLayout = LayoutInflater.from(getContext()).inflate(mSideLayoutRes, null);


        //
        addView(mSideLayout);
        addView(mHomeLayout);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //获取宽度
        mLayoutWidth = right;
        mSideWidth = (int) (3 / 5.0f * mLayoutWidth);

        /**
         * 设置主界面和侧边栏的位置
         */
        //主界面布局
        mHomeLayout.layout(left, top, right, bottom);

        //侧边栏的布局
        ViewGroup.LayoutParams params = mSideLayout.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        params.width = mSideWidth;
        mSideLayout.setLayoutParams(params);
        mSideLayout.layout(-mSideWidth, top, 0, bottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }

    private float mTouchDownX; //手指按下的x坐标
    private float mOldMoveX; //手指移动的上一个x坐标

    /**
     * 触摸响应
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = event.getX();
                mOldMoveX = mTouchDownX;
                break;

            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                moveLayout(moveX);
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                autoMove(event.getX());
                break;
        }
        return true;
    }

    /**
     * 移动布局
     */
    private void moveLayout(float moveX) {
        //计算位移
        float distanceX = moveX - mOldMoveX;
        mOldMoveX = moveX;

        //判断是否可以移动
        if (canMove(distanceX)) {
            //设置移动的位置
            mHomeLayout.setX(mHomeLayout.getX() + distanceX);
            mSideLayout.setX(mSideLayout.getX() + distanceX);

            //设置主界面的缩放
            float scale = 1 + ((mHomeLayoutScaleRatio - 1) / mSideWidth) * mHomeLayout.getX();
            mHomeLayout.setScaleX(scale);
            mHomeLayout.setScaleY(scale);
        }
    }

    /**
     * 判断布局是否可以移动
     *
     * @return true ：可以， false：不可以
     */
    private boolean canMove(float distance) {
        float nowX = mHomeLayout.getX() + distance;
        if (nowX <= mSideWidth && nowX >= 0)
            return true;
        else return false;
    }


    /**
     * 自动移动布局
     */
    private void autoMove(float upX) {
        if (isAutoShow(upX)) {
            autoShowSide();
        } else autoHideSide();
    }

    /**
     * 是否自动显示
     *
     * @return true :自动显示
     */
    private boolean isAutoShow(float upX) {
        float x = mHomeLayout.getX();
        if (x < mSideWidth && x >= mSideWidth / 2)
            return true;
        else return false;
    }

    /**
     * 自动显示侧边栏
     */
    public void autoShowSide() {
        //
        isShowingSide = true;
        //
        DecelerateInterpolator dl = new DecelerateInterpolator();  //减速
        /**
         * 主界面滑动
         */
        ObjectAnimator translationHome = ObjectAnimator.ofFloat(mHomeLayout, "translationX",
                mHomeLayout.getTranslationX(), mSideWidth);
        translationHome.setInterpolator(dl);
        translationHome.setDuration(mAnimateTime);

        /**
         * 侧边栏滑动
         */
        ObjectAnimator translationSide = ObjectAnimator.ofFloat(mSideLayout, "translationX",
                mSideLayout.getTranslationX(), mSideWidth + mLayoutWidth * (1 - mHomeLayoutScaleRatio) / 4);
        translationSide.setInterpolator(dl);
        translationSide.setDuration(mAnimateTime);

        /**
         * 主界面缩放
         */
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mHomeLayout, "scaleX",
                mHomeLayout.getScaleX(), mHomeLayoutScaleRatio);
        translationSide.setInterpolator(dl);
        translationSide.setDuration(mAnimateTime);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mHomeLayout, "scaleY",
                mHomeLayout.getScaleY(), mHomeLayoutScaleRatio);
        translationSide.setInterpolator(dl);
        translationSide.setDuration(mAnimateTime);


        /**
         * 开始动画
         */
        translationHome.start();
        translationSide.start();

        scaleX.start();
        scaleY.start();

        //
        mHomeLayout.setFocusableInTouchMode(false);
    }

    /**
     * 自动隐藏侧边栏
     */
    public void autoHideSide() {
        //
        isShowingSide = false;
        //
        DecelerateInterpolator dl = new DecelerateInterpolator();  //减速
        /**
         * 主界面滑动
         */
        ObjectAnimator translationHome = ObjectAnimator.ofFloat(mHomeLayout, "translationX",
                mHomeLayout.getTranslationX(), 0);
        translationHome.setInterpolator(dl);
        translationHome.setDuration(mAnimateTime);

        /**
         * 侧边栏滑动
         */
        ObjectAnimator translationSide = ObjectAnimator.ofFloat(mSideLayout, "translationX",
                mSideLayout.getTranslationX(), -mSideWidth);
        translationSide.setInterpolator(dl);
        translationSide.setDuration(mAnimateTime);

        /**
         * 主界面缩放
         */
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mHomeLayout, "scaleX",
                mHomeLayout.getScaleX(), 1);
        translationSide.setInterpolator(dl);
        translationSide.setDuration(mAnimateTime);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mHomeLayout, "scaleY",
                mHomeLayout.getScaleY(), 1);
        translationSide.setInterpolator(dl);
        translationSide.setDuration(mAnimateTime);

        /**
         * 开始动画
         */
        translationHome.start();
        translationSide.start();

        scaleX.start();
        scaleY.start();
    }

    /**
     * 获取主界面view
     *
     * @return
     */
    public View getHomeLayout() {
        return mHomeLayout;
    }

    /**
     * 获取侧边栏view
     *
     * @return
     */
    public View getSideLayout() {
        return mSideLayout;
    }

    /**
     * 侧边栏是否正在显示
     *
     * @return
     */
    public boolean isShowingSide() {
        return isShowingSide;
    }
}
