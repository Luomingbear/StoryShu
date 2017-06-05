package com.storyshu.storyshu.widget.card;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

/**
 * 实现卡片的左右滑动效果的布局管理
 * Created by bear on 2017/6/5.
 */

public class CardSlideLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "CardSlideLayoutManager";
    private int mLeftIndex = 0; //左边的下标，可见范围
    private int mCenterIndex = 0; //左边的下标，可见范围
    private int mRightIndex = 0; //右边的下标，可见范围
    private int mMaxCardShow = 3; //
    private int mMaxCardCache = 5; //

    private float mScale = 0.8f; //卡片的缩放比例
    private float mCenterLeftX = 0; //中心卡片的左下角下标
    private float mCenterWidth = 0; //中心卡片的宽度
    private RecyclerView.Recycler mRecycler;

    public CardSlideLayoutManager() {

    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 更细下标
     */
    private void updateIndex() {
        mLeftIndex = mCenterIndex - mMaxCardCache / 2; //用中心卡片的下标减去最大缓存的卡片数量的一半就是最左边的卡片的下标
        mLeftIndex = Math.max(mLeftIndex, 0); //大于0

        mRightIndex = mLeftIndex + (mLeftIndex == mCenterIndex ? mMaxCardCache / 2 : mMaxCardCache - 1);
        mRightIndex = Math.min(mRightIndex, getItemCount() - 1); //小于最大数量
    }

    /**
     * 对卡片进行布局
     *
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        Log.d(TAG, "onLayoutChildren: !!!!!!!!!!!");
        detachAndScrapAttachedViews(recycler);

        int count = getItemCount();
        if (count <= 0)
            return;

        //更细下标
        updateIndex();

        //添加
        for (int i = mLeftIndex; i <= mRightIndex; i++) {
            //获取view
            View view = recycler.getViewForPosition(i);
            //添加view
            addView(view);

            /**
             * 设置卡片的大小
             */
            boolean isNeedMeasure = view.isLayoutRequested();
            if (isNeedMeasure) {
                int itemWidth = (int) (getWidth() * mScale);
                int itemHeight = (getHeight());
                view.measure(View.MeasureSpec.EXACTLY | itemWidth, View.MeasureSpec.EXACTLY | itemHeight);
            }

            positionCard(view, i);
//            detachAndScrapView(view, recycler);
        }

//        detachAndScrapAttachedViews(recycler);
    }

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

        /**
         * 缩放卡片到合适的尺寸
         */
        float ratio = getHeight() / (float) height;
        float WidthRatio = ratio * (1 - 0.1f);
        float showWidth = width * WidthRatio * 1.11f; //获取当前卡片缩放之后的宽度

        //
        int left = (int) ((getWidth() - width) / 2 + showWidth * (index - mCenterIndex));
        int top = (getHeight() - height) / 2;
        card.layout(left, top, left + width, top + height);


        /**
         * 设置卡片缩放
         */
        index = Math.abs(index - mCenterIndex); //
        index = Math.min(index, 1); //只有中心的卡片的大小会大一些，其他位置一样的大小
        ratio = ratio * (1 - 0.15f * index);
//        Log.e(TAG, "positionCard: Ratio:" + ratio);
        card.setScaleX(ratio);
        card.setScaleY(ratio);

        if (index == mCenterIndex) {
            mCenterLeftX = left;
            mCenterWidth = showWidth;
        }

    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        offsetChildrenHorizontal(-dx);
        scaleCards();
        return dx;
    }


    /**
     * 移动卡片
     *
     * @param distanceX
     */
    private void moveCards(float distanceX) {
        int i;
        int count = getChildCount();
        for (i = 0; i < Math.min(count, mLeftIndex + mMaxCardCache); i++) {
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
                scale = 1 * (1 - 0.15f * 1);
            } else {
                scale = 1 * (1 - 0.15f * (distance / mCenterWidth)); //计算卡片的缩放比例，通过卡片与中心点的距离
            }

            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }

    private int changIndex; //改变的下标 不为0则说明改变了
    private int symbol; //大于0则向右滑
    private float minDistance; // 卡片距离中心卡片的最小距离

    /**
     * 计算中心卡片的下标并且让卡片自动移动到恰当的位置
     *
     * @param moveDistance 手指滑动的距离
     */
    void animationMoveCards(float moveDistance) {

        /**
         * 计算加速度
         */
        float acceleration = Math.abs(moveDistance / 0); //加速度

        symbol = moveDistance > 0 ? -1 : 1; //如果大于0则说明手指是向右滑动，反之

        /**
         * 计算中心卡片的下标
         */
        changIndex = Math.abs(acceleration) > 0.7f ? 1 : 0; //手指滑动的加速度大于0.7就可以改变中心卡片的下标
        if (acceleration <= 0.7)
            changIndex = Math.abs(moveDistance) > mCenterWidth / 4 ? 1 : 0; //手指滑动的距离大于卡片的宽度的1／4就可以改变中心卡片的下标

        mCenterIndex = mCenterIndex + changIndex * symbol; //之前的中心卡片下标加上手指滑动的卡片数量，乘以符号是判断滑动的方向
        mCenterIndex = Math.max(mCenterIndex, 0); //不能为负数
        mCenterIndex = Math.min(mCenterIndex, getChildCount() - 1); //不能超过卡片的总数

        /**
         * 计算移动的距离
         */
        if (changIndex == 0) { //移动的距离小于卡片的1／4则弹回去
            minDistance = -moveDistance;
        } else if (Math.abs(moveDistance) > mCenterWidth / 4 && Math.abs(moveDistance) <= mCenterWidth) {  //移动的距离小于卡片的宽度并且大于1／4
            minDistance = -(mCenterWidth - Math.abs(moveDistance)) * symbol;
        } else {
            minDistance = (Math.abs(moveDistance) - mCenterWidth) * symbol;
        }

        /**
         * 自动移动
         */

        if (minDistance == 0)
            return;

        int i;
        for (i = getChildCount() - 1; i >= 0; i--) {
            View card = getChildAt(i);
            if (card == null)
                continue;

            //清除动画
            card.clearAnimation();

            /**
             * 位移动画
             * 动画执行的时间和滑动的加速度有关
             *
             */
            //利用指数函数实现,在第一象限,无限接近0，0点的值为1，所以加速度越大，时间越小
            double sTime = Math.pow(0.8, acceleration);
            sTime = Math.max(sTime, 0.6); //最快也不能小于标准的0.6

            /**
             * 设置位移动画
             */
            DecelerateInterpolator dl = new DecelerateInterpolator();  //减速

            ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(card, "translationX",
                    card.getTranslationX(), card.getTranslationX() + minDistance);
            translationAnimator.setInterpolator(dl);
            translationAnimator.setDuration((long) (200 * sTime));

            /**
             * 添加缩放动画，设置缩放的比例
             */
            //控制卡片的缩放比例
//            int index = i == rightCardIndex - centerCardIndex ? 0 : 1;
//            Log.i(TAG, "animationMoveCards: i:" + i + "      index:" + index + "      center:" + centerCardIndex + "      right:" + rightCardIndex);
//            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(card, "scaleX",
//                    card.getScaleX(), mRatio * (1 - 0.15f * index));
//            scaleXAnimator.setInterpolator(dl);
//            scaleXAnimator.setDuration((long) (spendTime * sTime));


            /**
             * 设置阴影动画
             */


            /**
             * 开始动画
             */
            translationAnimator.start();
//            scaleXAnimator.page();

            /**
             * 设置动画监听器
             */
//            if (i == 0)
//                translationAnimator.addListener(mCardAnimationListener);
        }

        /**
         * 更新左右端点的下标
         */
//        oldCenterCardIndex = centerCardIndex;
//        updateIndex();
    }

    /**
     * 添加滑动
     *
     * @return
     */
    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
