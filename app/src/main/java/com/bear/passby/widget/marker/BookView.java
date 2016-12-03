package com.bear.passby.widget.marker;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bear.passby.R;

/**
 * 故事集图标
 * Created by bear on 2016/12/2.
 */

public class BookView extends RelativeLayout {
    private int mWidth; //宽度
    private TextView mTitleView; //图标上的文本
    private String mTitleString; //图表文本
    private float mTitleSize; //图标字体大小
    private int mTitleColor; //图标字的颜色

    public BookView(Context context) {
        super(context);
        init();
    }

    public BookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mWidth = (int) getResources().getDimension(R.dimen.image_small);
        mTitleColor = getResources().getColor(R.color.colorGreenScallionLight);
        mTitleSize = getResources().getDimension(R.dimen.font_small);
        drawBg();

        drawText();
    }

    /**
     * 背景
     */
    private void drawBg() {
        ImageView bgIV = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(mWidth, mWidth);
        bgIV.setLayoutParams(layoutParams);
        bgIV.setBackgroundResource(R.drawable.book);
        addView(bgIV);
    }

    /**
     * 绘制标题文本
     */
    private void drawText() {
//        if (TextUtils.isEmpty(mTitleString))
//            return;
        mTitleView = new TextView(getContext());
        LayoutParams p = new LayoutParams(mWidth, LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTitleView.setLayoutParams(p);

        mTitleView.setEllipsize(TextUtils.TruncateAt.END);
        mTitleView.setSingleLine();
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
        mTitleView.setTextColor(mTitleColor);
        mTitleView.setText(mTitleString);
        addView(mTitleView);
    }

    public void setTitleColor(int mTitleColor) {
        this.mTitleColor = mTitleColor;
        mTitleView.setTextColor(mTitleColor);
        invalidate();
    }

    public void setTitleSize(float mTitleSize) {
        this.mTitleSize = mTitleSize;
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
        invalidate();
    }

    public void setTitleString(String mTitleString) {
        this.mTitleString = mTitleString;
        mTitleView.setText(mTitleString);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = (int) getResources().getDimension(R.dimen.image_small);
        setMeasuredDimension(mWidth, mWidth);
    }
}
