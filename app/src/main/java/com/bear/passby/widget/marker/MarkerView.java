package com.bear.passby.widget.marker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bear.passby.R;

/**
 * 标志view
 * Created by bear on 2016/12/2.
 */

public class MarkerView extends RelativeLayout {
    private int mWidth; //宽度

    public MarkerView(Context context) {
        super(context);
        init();
    }

    public MarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarkerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mWidth = (int) getResources().getDimension(R.dimen.icon_normal);
        drawBg();
    }

    /**
     * 背景
     */
    private void drawBg() {
        ImageView bgIV = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(mWidth, mWidth);
        bgIV.setLayoutParams(layoutParams);
        bgIV.setBackgroundResource(R.drawable.person_location);
        addView(bgIV);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = (int) getResources().getDimension(R.dimen.icon_normal);
        setMeasuredDimension(mWidth, mWidth);
    }
}
