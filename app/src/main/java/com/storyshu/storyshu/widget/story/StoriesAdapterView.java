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

import java.util.ArrayList;

/**
 * 自定义的卡片滑动控件，根据数据自动生成卡片布局
 * Created by bear on 2016/12/4.
 */

public class StoriesAdapterView extends AdapterView {
    private static final String TAG = "StoriesAdapterView";
    private AdapterDataSetObserver mDataSetObserver; //自定义DataSetObserver
    private Adapter mAdapter; //适配器
    private boolean isLayout = false; //是否正在布局
    private View mCenterCard; //中心的卡片
    private int MaxCardCacheNum = 3; //屏幕中缓存的最大的卡片数量
    private int leftCardIndex = 0; //屏幕上最左边的卡片的下标
    private int centerCardIndex = 0; //屏幕中心的卡片的下标
    private int oldCenterCardIndex = 0; //上一次的屏幕中心的卡片的下标
    private int rightCardIndex = 0; //屏幕上最右边的卡片的下标
    private float minDistance; // 卡片距离中心卡片的最小距离

    private boolean isFastMove = true; //是否快速滑动
    private long spendTime = 350; //动画的执行时间
    private ArrayList<ViewHolder> mViewList = new ArrayList<>(); //存放显示的卡片

    private class ViewHolder {
        View view; //view
        int index; //下标

        public ViewHolder(View view, int index) {
            this.view = view;
            this.index = index;
        }
    }

    public StoriesAdapterView(Context context) {
        super(context);

    }

    public StoriesAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public StoriesAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void init(Adapter cardAdapter) {
//        if (mContext instanceof OnCardSlidingListener)
//            onCardSlidingListener = (OnCardSlidingListener) mContext;
//        else
//            throw new RuntimeException("Activity 没有继承OnCardSlidingListener！");

        setAdapter(cardAdapter);
    }

    /**
     * 设置显示的中心卡片下标
     *
     * @param centerCardIndex
     */
    public void setCenterCardIndex(int centerCardIndex) {
        this.centerCardIndex = centerCardIndex;
        updateIndex();
        requestLayout();
    }

    /**
     * 数据更新观察者
     */
    private class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            Log.i(TAG, "onDeleteClick: ");
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

        Log.d(TAG, "onLayout: !!!!!!!!!!!!!!!!!!!");
        isLayout = true;
        int count = mAdapter.getCount(); //数据的数量

//        removeAllViewsInLayout(); //移除之前加载的卡片，避免重复绘制

        /**如果卡片数量为0则直接退出
         *
         */
        if (count == 0)
            return;

        //更新下标
        updateIndex();

        /**
         * 快速滑动则添加所有的卡片
         * 、否则只添加变化的
         */
//        if (isFastMove)
//            layoutAllCards(count);
//        else
//            layoutDefCards(count);

        layoutAllCards(count);

        //
        isLayout = false;
    }

    /**
     * 返回当前的这个下标的卡片是否是已经显示了
     *
     * @param index
     * @return
     */
    private boolean isCardLayouted(int index) {
        for (ViewHolder vh : mViewList) {
            if (vh.index == index)
                return true;
        }
        return false;
    }


    /**
     * 仅绘制变化的卡片
     */
    private void layoutDefCards(int count) {
        int index = leftCardIndex;

        /**
         * 清除不需要的卡片
         */
        removeViews();

        /**
         * 生成卡片
         */
        while (index <= Math.min(count - 1, rightCardIndex)) {
            //列表空说明还没有加载卡片的，所以要加载
            if (mViewList.size() == 0)
                addAndMeasureCard(index);
                //index-leftIndex = 当前是加载的第几个，不是卡片下标，只是加载的顺序而已
            else if (index - leftCardIndex > mViewList.size())
                addAndMeasureCard(index);
                //如果已经加载到了最大的缓冲卡片数量则需要对不需要的卡片重新填充
            else if (index - leftCardIndex <= mViewList.size()) {
                //移除不需要的
                if (!isCardLayouted(index))
                    addAndMeasureCard(index);
            }
            index++;
        }
    }

    /**
     * 绘制所有的卡片
     */
    private void layoutAllCards(int count) {
        int index = leftCardIndex;

        /**
         * 移除之前的所有卡片
         */
        removeAllViewsInLayout();
        mViewList.clear();

        /**
         * 生成卡片
         */
        while (index <= Math.min(count - 1, rightCardIndex)) {
            addAndMeasureCard(index);
            index++;
        }
    }

    /**
     * 移除不需要的view
     */
    private void removeViews() {
        int i = 0;
        while (true) {
            if (mViewList.size() == 0)
                break;

            if (mViewList.get(i).index < leftCardIndex || mViewList.get(i).index > rightCardIndex) {
                Log.e(TAG, "removeViews: 移除卡片!!!!+++" + mViewList.get(i).index);
                removeViewInLayout(mViewList.get(i).view);
                mViewList.remove(i);
                i = 0;
            } else {
                i++;
                if (i >= mViewList.size())
                    break;
            }
        }
    }

    /**
     * 添加单个故事卡片
     */
    private void addAndMeasureCard(int index) {
        Log.i(TAG, "layoutDefCards: 生成卡片！！+++" + index);

        //add
        View card = mAdapter.getView(index, null, this);
        if (index == centerCardIndex) {
            mCenterCard = card;
//            card.findViewById(R.id.card_layout).setElevation(getResources().getDimension(R.dimen.height_normal));
        }
        LayoutParams params = card.getLayoutParams();
        if (params == null)
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addViewInLayout(card, 0, params, true);

        /**
         * 设置卡片的大小
         */
        boolean isNeedMeasure = card.isLayoutRequested();
        if (isNeedMeasure) {
            int itemWidth = (int) (getWidth() * 0.9f);
            int itemHeight = (getHeight());
            card.measure(MeasureSpec.EXACTLY | itemWidth, MeasureSpec.EXACTLY | itemHeight);
        }

        //location
        positionCard(card, index);

        /**
         * 保存卡片的数据
         */
        ViewHolder viewHolder = new ViewHolder(card, index);
        mViewList.add(viewHolder);

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

        /**
         * 卡片的大小有时候会超过给定的空间，所以需要在显示之后缩放
         */

        //高宽比
        float ratio = getHeight() / (float) height / 1.35f;
        mRatio = ratio;
        /**
         * 缩放卡片到合适的尺寸
         */
//        float WidthRatio = ratio * (1 - 0.08f);
        float WidthRatio = (1 - 0.08f);
        float showWidth = width * WidthRatio * 1.11f; //获取当前卡片缩放之后的宽度

        //
        int left = (int) ((getWidth() - width) / 2 + showWidth * (index - centerCardIndex));
        int top = (getHeight() - height) / 2;
        card.layout(left, top, left + width, top + height);

        //纪录屏幕中心卡片的左端点和右端点
        if (index == centerCardIndex) {
            mCenterLeftX = left;
            mCenterWidth = showWidth;
        }

        //卡片的大小缩放
        index = Math.abs(index - centerCardIndex); //

        index = Math.min(index, 1); //只有中心的卡片的大小会大一些，其他位置一样的大小
        ratio = ratio * (1 - 0.15f);
//        Log.e(TAG, "positionCard: Ratio:" + ratio);
//        card.setScaleX(WidthRatio);
//        card.setScaleY(WidthRatio);

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
//                Log.e(TAG, "onTouchEvent: ACTION_DOWN");
                downX = event.getX();
                oldX = downX;

                /**
                 * 是否是快速的滑动
                 * 快速滑动的话布局时将之前的卡片全部删除
                 */
                isFastMove = System.currentTimeMillis() - mStartTime < 250;

                mStartTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
//                Log.e(TAG, "onTouchEvent: ACTION_MOVE: " + moveX);
                distanceX = (moveX - oldX);
                oldX = moveX;
                moveCards(distanceX);
//                scaleCards();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                float upY = event.getY();

                //手指移动的距离小于5并且手指在卡片上
                if (Math.abs(upX - downX) < 5) {
                    //ka卡片数量为0则关闭弹窗
                    if (mAdapter == null || mAdapter.getCount() == 0) {
                        if (onCardClickListener != null)
                            onCardClickListener.onDismissClick();
                        break;
                    }
                    if (upY < (getHeight() + mCenterCard.getHeight()) / 2 &&
                            upY > (getHeight() - mCenterCard.getHeight()) / 2)
                        onCardClick();
                    else {
                        if (onCardClickListener != null)
                            onCardClickListener.onDismissClick();
                    }

                } else {
                    long endTime = System.currentTimeMillis(); //
                    mMoveTime = endTime - mStartTime; //计算手指在屏幕上的时间
                    if (mAdapter == null || mAdapter.getCount() == 0)
                        break;
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
        for (i = 0; i < Math.min(count, leftCardIndex + MaxCardCacheNum); i++) {
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
            isLayout = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {

            /**
             * 卡片的回调函数
             */
            if (onCardSlidingListener != null) {
                onCardSlidingListener.onAfterSlideCenterIndex(centerCardIndex);
                onCardSlidingListener.onAfterSlideLeftIndex(leftCardIndex);
            }

            isLayout = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };


    private int changIndex; //改变的下标 不为0则说明改变了
    private int symbol; //大于0则向右滑

    /**
     * 计算中心卡片的下标并且让卡片自动移动到恰当的位置
     *
     * @param moveDistance 手指滑动的距离
     */
    void animationMoveCards(float moveDistance) {

        /**
         * 计算加速度
         */
        float acceleration = Math.abs(moveDistance / mMoveTime); //加速度

        symbol = moveDistance > 0 ? -1 : 1; //如果大于0则说明手指是向右滑动，反之

        /**
         * 计算中心卡片的下标
         */
        changIndex = Math.abs(acceleration) > 0.7f ? 1 : 0; //手指滑动的加速度大于0.7就可以改变中心卡片的下标
        if (acceleration <= 0.7)
            changIndex = Math.abs(moveDistance) > mCenterWidth / 4 ? 1 : 0; //手指滑动的距离大于卡片的宽度的1／4就可以改变中心卡片的下标

        centerCardIndex = centerCardIndex + changIndex * symbol; //之前的中心卡片下标加上手指滑动的卡片数量，乘以符号是判断滑动的方向
        centerCardIndex = Math.max(centerCardIndex, 0); //不能为负数
        centerCardIndex = Math.min(centerCardIndex, mAdapter.getCount() - 1); //不能超过卡片的总数

        /**
         * 计算移动的距离
         */
        if (oldCenterCardIndex != centerCardIndex) {
            if (changIndex == 0) { //移动的距离小于卡片的1／4则弹回去
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
            translationAnimator.setDuration((long) (spendTime * sTime));

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
//            scaleXAnimator.start();

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
//        updateIndex();
    }

    /**
     * 更新卡片
     */
    private void updateIndex() {
        leftCardIndex = centerCardIndex - MaxCardCacheNum / 2; //用中心卡片的下标减去最大缓存的卡片数量的一半就是最左边的卡片的下标
        leftCardIndex = Math.max(leftCardIndex, 0); //大于0

        rightCardIndex = leftCardIndex + (leftCardIndex == centerCardIndex ? MaxCardCacheNum / 2 : MaxCardCacheNum - 1);
        rightCardIndex = Math.min(rightCardIndex, mAdapter.getCount() - 1); //小于最大数量

//        Log.d(TAG, "  leftIndex:" + leftCardIndex);
//        Log.d(TAG, "  centerIndex:" + centerCardIndex);
//        Log.d(TAG, "  rightIndex:" + rightCardIndex);
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
        return mCenterCard;
    }

    @Override
    public void setSelection(int position) {
        if (position > mAdapter.getCount() || position < 0)
            return;

        centerCardIndex = position;

        leftCardIndex = centerCardIndex - MaxCardCacheNum / 2; //用中心卡片的下标减去最大缓存的卡片数量的一半就是最左边的卡片的下标
        leftCardIndex = Math.max(leftCardIndex, 0); //大于0

        rightCardIndex = leftCardIndex + MaxCardCacheNum - 1;
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
        void onAfterSlideCenterIndex(int position);

        //卡片的最左边的下标
        void onAfterSlideLeftIndex(int leftIndex);
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

        void onDismissClick(); //点击了非卡片区域
    }
}