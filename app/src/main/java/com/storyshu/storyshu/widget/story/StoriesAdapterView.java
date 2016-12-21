package com.storyshu.storyshu.widget.story;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * 自定义的卡片滑动控件，根据数据自动生成卡片布局
 * Created by bear on 2016/12/4.
 */

public class StoriesAdapterView extends AdapterView {
    private static final String TAG = "StoriesAdapterView";
    private Context mContext; //
    private AdapterDataSetObserver mDataSetObserver; //自定义DataSetObserver
    private Adapter mAdapter; //适配器
    private boolean isLayout = false; //是否正在布局
    private View mCenterCard; //中心的卡片
    private int MaxCardShowNum = 5; //屏幕中缓存的最大的卡片数量
    private int MaxCardCacheNum = 5; //屏幕中缓存的最大的卡片数量
    private int leftCardIndex = 0; //屏幕上最左边的卡片的下标
    private int centerCardIndex = 0; //屏幕中心的卡片的下标
    private int oldCenterCardIndex = 0; //上一次的屏幕中心的卡片的下标
    private int rightCardIndex = MaxCardShowNum - 1; //屏幕上最右边的卡片的下标
    private float minDistance; // 卡片距离中心卡片的最小距离
    private float[] mLeftPoints = new float[MaxCardShowNum]; //卡片的左端点x坐标集合
    private long spendTime = 150; //动画的执行时间


    public StoriesAdapterView(Context context) {
        super(context);
        mContext = context;

    }

    public StoriesAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    public StoriesAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

    }

    public void init(Adapter cardAdapter) {
//        if (mContext instanceof OnCardSlidingListener)
//            onCardSlidingListener = (OnCardSlidingListener) mContext;
//        else
//            throw new RuntimeException("Activity 没有继承OnCardSlidingListener！");
        setAdapter(cardAdapter);
    }

    /**
     * 数据更新观察者
     */
    private class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            Log.i(TAG, "onChanged: ");
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            Log.i(TAG, "onInvalidated: ");
            requestLayout();
        }

    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }
        mAdapter = adapter;

        if (mAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mAdapter == null)
            return;

        //
        Log.d(TAG, "onLayout: !!!!!!!!!!!!!!!!!!!");
        isLayout = true;
        // TODO: 2016/12/4 卡片生成的逻辑
        int count = mAdapter.getCount(); //数据的数量
        removeAllViewsInLayout(); //移除之前加载的卡片，避免重复绘制

        /**如果卡片数量为0则直接退出
         *
         */
        if (count == 0)
            return;

        layoutCards(count);

        //
        isLayout = false;

    }

    /**
     * 添加故事集
     */
    private void layoutCards(int count) {
        Log.i(TAG, "layoutCards: LeftIndex:" + leftCardIndex);
        Log.i(TAG, "layoutCards: CenterIndex:" + centerCardIndex);
        Log.i(TAG, "layoutCards: RightIndex:" + rightCardIndex);
        int index = leftCardIndex;
        while (index < Math.min(count, leftCardIndex + MaxCardShowNum)) {
            addAndMeasureCard(index);
            index++;
        }
    }

    /**
     * 添加单个故事卡片
     */
    private void addAndMeasureCard(int index) {
        //add
        View card = mAdapter.getView(index, null, this);
        LayoutParams params = card.getLayoutParams();
        if (params == null)
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addViewInLayout(card, 0, params, true);

        //measureView
        boolean isNeedMeasure = card.isLayoutRequested();
        if (isNeedMeasure) {
            int itemWidth = getWidth();
            card.measure(MeasureSpec.EXACTLY | itemWidth, MeasureSpec.UNSPECIFIED);
        }

        //position
        positionCard(card, index);
    }

    private float mCenterLeftX, mCenterWidth; //屏幕中心的卡片的左端点和卡片的宽度
    private float mRatio; //卡片缩放比例

    /**
     * 设置位置
     *
     * @param card
     * @param index
     */
    private void positionCard(View card, int index) {
        if (card == null)
            return;

        int width = card.getMeasuredWidth();
        int height = card.getMeasuredHeight();
//        Log.e(TAG, "positionCard: ratio:" + (height / (float) width));

        /**
         * 卡片的大小有时候会超过给定的空间，所以需要在显示之后缩放
         */

        //高宽比
        float ratio = getHeight() / (float) height;
        mRatio = ratio;
        /**
         * 缩放卡片到合适的尺寸
         */
        float WidthRatio = ratio * (1 - 0.08f);
        float showWidth = width * WidthRatio; //获取当前卡片缩放之后的宽度

        //
        int left = (int) ((getWidth() - width) / 2 + showWidth * (index - centerCardIndex));
        int top = (getHeight() - height) / 2;
        card.layout(left, top, left + width, top + height);

        mLeftPoints[index - leftCardIndex] = left;//保存左端点的值

        //纪录屏幕中心卡片的左端点和右端点
        if (index == centerCardIndex) {
            mCenterLeftX = left;
            mCenterWidth = showWidth;
        }

        //卡片的大小缩放
        index = Math.abs(index - centerCardIndex); //

        index = Math.min(index, 1); //只有中心的卡片的大小会大一些，其他位置一样的大小
        ratio = ratio * (1 - 0.15f * index);
//        Log.e(TAG, "positionCard: Ratio:" + ratio);
        card.setScaleX(ratio);
        card.setScaleY(ratio);
        Log.i(TAG, "positionCard: oldX:" + card.getX());

        //记录下当前最小的距离值
        minDistance = mCenterWidth;
    }

    private float downX; //按下的x
    private float downY; //按下的y
    private float distanceX;
    private float oldX;
    private long mStartTime; //手指放上去的时间
    private float mMoveTime; //手指滑动的时间

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent: ACTION_DOWN");
                downX = event.getX();
                oldX = downX;
                mStartTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
//                Log.e(TAG, "onTouchEvent: ACTION_MOVE: " + moveX);
                distanceX = (moveX - oldX);
                oldX = moveX;
                moveCards(distanceX);
                scaleCards();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                if (Math.abs(upX - downX) < 5) {
                    onCardClick();

                } else {
                    long endTime = System.currentTimeMillis(); //
                    mMoveTime = endTime - mStartTime; //计算手指在屏幕上的时间
                    animationMoveCards(upX - downX); //卡片的自动移动效果
                }
                break;
        }

        return true;
    }

    /**
     * 移动卡片
     *
     * @param distanceX
     */
    private void moveCards(float distanceX) {
        int i;
        int count = getChildCount();
        for (i = 0; i < Math.min(count, leftCardIndex + MaxCardShowNum); i++) {
            View child = getChildAt(i);
            float x = child.getX() + distanceX;
            child.setX(x);
        }
    }

    /**
     * 缩放卡片
     */
    private void scaleCards() {
        int i, count;
        count = getChildCount();
        for (i = 0; i < count; i++) {
            View child = getChildAt(i);
            float x = child.getX();
            float scale;
            float distance = Math.abs(x - mCenterLeftX);
            if (distance > mCenterWidth) {
                //卡片的大小缩放
                scale = mRatio * (1 - 0.15f * 1);
            } else {
                scale = mRatio * (1 - 0.15f * (distance / mCenterWidth)); //计算卡片的缩放比例，通过卡片与中心点的距离
            }

            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }

    /**
     * 当卡片点击的时候执行
     */
    private void onCardClick() {
        if (onCardClickListener != null)
            onCardClickListener.OnCardClick(centerCardIndex);
    }

    /**
     * 卡片的动画回调
     */
    private Animator.AnimatorListener mCardAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            /**
             * 卡片的回调函数
             */
            if (onCardSlidingListener != null) {
                onCardSlidingListener.onCardLayouted(centerCardIndex);
                onCardSlidingListener.onLeftIndex(leftCardIndex);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    /**
     * 计算中心卡片的下标并且让卡片自动移动到恰当的位置
     *
     * @param moveDistance 手指滑动的距离
     */
    void animationMoveCards(float moveDistance) {
        Log.i(TAG, "animationMoveCards: animation");
        int symbol = moveDistance > 0 ? -1 : 1; //如果大于0则说明手指是向右滑动，反之


        /**
         * 计算中心卡片的下标
         */
        int changIndex = Math.abs(moveDistance) > mCenterWidth / 4 ? 1 : 0; //手指滑动的距离大于卡片的宽度的1／4就可以改变中心卡片的下标
        centerCardIndex = centerCardIndex + changIndex * symbol; //之前的中心卡片下标加上手指滑动的卡片数量，乘以符号是判断滑动的方向
        centerCardIndex = Math.max(centerCardIndex, 0); //不能为负数
        centerCardIndex = Math.min(centerCardIndex, mAdapter.getCount() - 1);//不能超过卡片的总数

        Log.e(TAG, "animationMoveCards: centerIndex:" + centerCardIndex);
        Log.e(TAG, "animationMoveCards: rightIndex:" + rightCardIndex);
        /**
         * 计算移动的距离
         */
        // TODO: 2016/12/14 计算移动的距离
        if (oldCenterCardIndex != centerCardIndex) {
            if (Math.abs(moveDistance) <= mCenterWidth / 4) { //移动的距离小于卡片的1／4则弹回去
                minDistance = -moveDistance;
            } else if (Math.abs(moveDistance) > mCenterWidth / 4 && Math.abs(moveDistance) <= mCenterWidth) {  //移动的距离小于卡片的宽度并且大于1／4
                minDistance = -(mCenterWidth - Math.abs(moveDistance)) * symbol;
            } else {
                minDistance = (Math.abs(moveDistance) - mCenterWidth) * symbol;
            }
        } else {
            minDistance = -moveDistance;
        }

        /**
         * 自动移动
         */
        Log.d(TAG, "animationMoveCards: MinDistance:" + minDistance);
        Log.d(TAG, "animationMoveCards: moveTime:" + mMoveTime);

        if (minDistance == 0)
            return;
        //只有中心卡片的左右两张卡片是需要移动的，其他卡片是看不见的，所以不是遍历每一张卡片
        //最大的显示卡片数量除以2得到的是中心卡片的一侧有几张卡片
        int i;
        for (i = getChildCount() - 1; i >= 0; i--) {
            // TODO: 2016/12/14 自动移动距离是distance；
            View card = getChildAt(i);
            if (card == null)
                continue;

            Log.i(TAG, "animationMoveCards: getScaleX:" + card.getScaleX() + " index:" + i);

            //清除动画
            card.clearAnimation();

            /**
             * 位移动画
             */
            DecelerateInterpolator dl = new DecelerateInterpolator();  //减速

            ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(card, "translationX",
                    card.getTranslationX(), card.getTranslationX() + minDistance);
            translationAnimator.setInterpolator(dl);
            translationAnimator.setDuration(spendTime);

            /**
             * 添加缩放动画，设置缩放的比例
             */
            //控制卡片的缩放比例
            int index = i == rightCardIndex - centerCardIndex ? 0 : 1;

            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(card, "scaleX",
                    card.getScaleX(), mRatio * (1 - 0.15f * index));
            scaleXAnimator.setInterpolator(dl);
            scaleXAnimator.setDuration(spendTime);
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(card, "scaleY",
                    card.getScaleY(), mRatio * (1 - 0.15f * index));
            scaleYAnimator.setInterpolator(dl);
            scaleYAnimator.setDuration(spendTime);


            /**
             * 开始动画
             */
            translationAnimator.start();
            scaleXAnimator.start();
            scaleYAnimator.start();

            /**
             * 设置动画监听器
             */
            if (i == 0)
                translationAnimator.addListener(mCardAnimationListener);
        }

        /**
         * 更新左右端点的下标
         */
        oldCenterCardIndex = centerCardIndex;
        leftCardIndex = centerCardIndex - MaxCardShowNum / 2; //用中心卡片的下标减去最大缓存的卡片数量的一半就是最左边的卡片的下标
        leftCardIndex = Math.max(leftCardIndex, 0); //大于0

        rightCardIndex = leftCardIndex + MaxCardShowNum - 1;
        rightCardIndex = Math.min(rightCardIndex, mAdapter.getCount() - 1); //小于最大数量

    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void requestLayout() {
        //如果没有在生成布局的话就可以执行刷新
        if (!isLayout)
            super.requestLayout();
    }

    @Override
    public View getSelectedView() {
        return getChildAt(centerCardIndex);
    }

    @Override
    public void setSelection(int position) {
        if (position > mAdapter.getCount() || position < 0)
            return;

        centerCardIndex = position;

        leftCardIndex = centerCardIndex - MaxCardShowNum / 2; //用中心卡片的下标减去最大缓存的卡片数量的一半就是最左边的卡片的下标
        leftCardIndex = Math.max(leftCardIndex, 0); //大于0

        rightCardIndex = leftCardIndex + MaxCardShowNum - 1;
        rightCardIndex = Math.min(rightCardIndex, mAdapter.getCount() - 1); //小于最大数量

    }

    private OnCardSlidingListener onCardSlidingListener;

    public void setOnCardSlidingListener(OnCardSlidingListener onCardSlidingListener) {
        this.onCardSlidingListener = onCardSlidingListener;
    }

    /**
     * 卡片的滑动接口回调
     */
    public interface OnCardSlidingListener {

        //卡片滑动到正确的位置之后就会调用这个函数，返回的是正中心选中的卡片的下标
        void onCardLayouted(int position);

        //卡片的最左边的下标
        void onLeftIndex(int leftIndex);
    }

    private OnCardClickListener onCardClickListener;

    public void setOnCardClickListener(OnCardClickListener onCardClickListener) {
        this.onCardClickListener = onCardClickListener;
    }

    /**
     * 卡片点击的回调
     */
    public interface OnCardClickListener {
        void OnCardClick(int cardIndex); //卡片的点击回调 返回卡片的下标
    }
}
