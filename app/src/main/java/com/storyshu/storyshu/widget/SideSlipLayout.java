package com.storyshu.storyshu.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.blurRelativeLayout.BlurRelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 侧滑布局
 * Created by bear on 2017/1/26.
 */

public class SideSlipLayout extends BlurRelativeLayout {
    private static final String TAG = "SideSlipLayout";
    private View mHomeLayout; //主界面
    private View mSideLayout; //侧边界面
    private int mHomeLayoutRes; //主界面的layout
    private int mSideLayoutRes;  //侧边的layout

    private int mLayoutWidth; //布局的宽度
    private int mSideWidth; //侧边栏的宽度
    private long mTouchTime; //手指按下的时间
    private long mSpendTime; //手指在屏幕上停留的时间

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
        mHomeLayoutRes = typedArray.getResourceId(R.styleable.SideSlipLayout_homeLayout, R.layout.story_map_layout);
        mSideLayoutRes = typedArray.getResourceId(R.styleable.SideSlipLayout_sideLayout, R.layout.activity_welcome_layout);
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

        //增加实时模糊
//        realtimeBlur();
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
        params.height = (int) (bottom * mHomeLayoutScaleRatio);
        mSideLayout.setLayoutParams(params);
        mSideLayout.layout(-mSideWidth, (int) ((1 - mHomeLayoutScaleRatio) / 2.0f * bottom), 0,
                (int) ((1 + mHomeLayoutScaleRatio) / 2 * bottom));
    }


    private float interceptDownX; //拦截触摸事件的手指按下的x坐标
    private float interceptDownY;

    /**
     * 更具情况拦截手指的触摸事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                interceptDownX = ev.getX();
                interceptDownY = ev.getY();
                mTouchTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:
                float x = ev.getX();
                /**
                 * 手指按下的时候在左边则拦截滑动事件
                 */
                if (!isShowingSide() && interceptDownX < mSideWidth / 4) {
                    if (Math.abs(x - interceptDownX) > 5) {
                        return true;
                    }
                } else if (isShowingSide()) {
                    if (Math.abs(x - interceptDownX) > 5) {
                        return true;
                    } else if (x >= mSideWidth && Math.abs(x - interceptDownX) < 5)
                        return true;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float upX = ev.getX();
                float upY = ev.getY();
                if (isShowingSide() && upX >= mSideWidth)
                    if (Math.abs(upX - interceptDownX) < 5 &&
                            Math.abs(upY - interceptDownY) < 5)
                        return true;
                break;
        }
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
                autoMove(event.getX() - interceptDownX);
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
        mSpendTime = System.currentTimeMillis() - mTouchTime;
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
        float a = upX / mSpendTime;
        Log.i(TAG, "isAutoShow: a:" + a);
        if (a > 0.7)
            return true;
        else if (a < 0.7)
            return false;
        else if (x < mSideWidth && x >= mSideWidth / 2)
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
//        realtimeBlur();

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
        translationHome.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
//                realtimeBlur();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                timer.cancel();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
//                timer.cancel();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
//                timer.cancel();
//                realtimeBlur();
            }
        });
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

    /**
     * 获取当前的主界面的位置比例
     *
     * @return
     */
    public float getSlipRatio() {
        return mHomeLayout.getX() / (float) mSideWidth;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setBlurRatio(getSlipRatio());

                    break;
            }
        }
    };

    private Timer timer;

    /**
     * 设置背景模糊实时显示
     */
    private void realtimeBlur() {
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, 0, 35);
    }
}
