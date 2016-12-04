package com.bear.passby.widget.story;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
    private int MaxCardNum; //屏幕可以显示的最大的卡片数量
    private int leftCardIndex = 0; //屏幕上最左边的卡片的下标
    private int rightCardIndex = 2; //屏幕上最右边的卡片的下标

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
            requestLayout();
        }

        @Override
        public void onInvalidated() {
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
        isLayout = true;
        // TODO: 2016/12/4 卡片生成的逻辑
        int count = mAdapter.getCount(); //数据的数量
        if (count == 0) {
            removeAllViewsInLayout();
        } else {
            //取得最大显示的卡片数量
            MaxCardNum = CardNumUtil.getMaxCardNumInScreen(
                    (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE),
                    mAdapter.getView(0, null, this));
            layoutCards(count);
        }
        //
        isLayout = false;

    }

    /**
     * 添加故事集
     */
    private void layoutCards(int count) {
        int index = 0;
        while (index < Math.min(count, Math.max(MaxCardNum, 3))) {
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
        Log.e(TAG, "positionCard: ratio:" + (height / (float) width));

        /**
         * 卡片的大小有时候会超过给定的空间，所以需要在显示之后缩放
         */

        //高宽比
        float ratio = getHeight() / (float) height;
        mRatio = ratio;
        /**
         * 缩放卡片到合适的尺寸
         */
        float WidthRatio = ratio * (1 - 0.05f);
        float showWidth = width * WidthRatio; //获取当前卡片缩放之后的宽度

        //
        int left = (int) ((getWidth() - width) / 2 + showWidth * index);
        int top = (getHeight() - height) / 2;

        card.layout(left, top, left + width, top + height);

        //纪录屏幕中心卡片的左端点和右端点
        if (index == 0) {
            mCenterLeftX = left;
            mCenterWidth = showWidth;
        }

        //卡片的大小缩放
        index = Math.min(index, 1);
        ratio = ratio * (1 - 0.15f * index);
        card.setScaleX(ratio);
        card.setScaleY(ratio);
    }

    private float downX; //按下的x
    private float downY; //按下的y
    private float distanceX;
    private float oldX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent: ACTION_DOWN");
                downX = event.getX();
                oldX = downX;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                Log.e(TAG, "onTouchEvent: ACTION_MOVE: " + moveX);
                distanceX = (moveX - oldX);
                oldX = moveX;
                moveCards(distanceX);
                scaleCards();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
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
        Log.i(TAG, "moveCards: count:" + count);
        for (i = 0; i < count; i++) {
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
                scale = mRatio * (1 - 0.15f * (distance / mCenterWidth));
            }

            child.setScaleX(scale);
            child.setScaleY(scale);
        }
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
        return null;
    }

    @Override
    public void setSelection(int position) {

    }

    private OnCardSlidingListener onCardSlidingListener;

    public interface OnCardSlidingListener {

    }
}
