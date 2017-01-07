package com.storyshu.storyshu.widget.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.DipPxConversion;
import com.storyshu.storyshu.widget.imageview.RoundImageView;

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
        this(context, null);
    }

    public BookView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWidth = (int) getResources().getDimension(R.dimen.image_small);
        mTitleColor = getResources().getColor(R.color.colorGreenScallionLight);
        mTitleSize = getResources().getDimension(R.dimen.font_small);

        drawBg();
    }

    private void drawBg() {
        ImageView bgIV = new ImageView(getContext());
        LayoutParams p = new LayoutParams((int) (mWidth * 0.8f), mWidth);
        p.addRule(RelativeLayout.CENTER_HORIZONTAL);
        p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        bgIV.setLayoutParams(p);
        bgIV.setBackgroundResource(R.drawable.person_marker_bg);
        addView(bgIV);

    }

    /**
     * 背景
     */
    public void init(Bitmap bitmap) {
        RoundImageView bgIV = new RoundImageView(getContext());
        int width = (int) (mWidth * 0.78);
        LayoutParams layoutParams = new LayoutParams((int) (width * 0.8), width);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        bgIV.setBorderRadius(DipPxConversion.dip2px(getContext(), 3));
        bgIV.setLayoutParams(layoutParams);
        bgIV.setImageBitmap(bitmap);
        addView(bgIV);
        //
        drawText();

    }

    /**
     * 背景
     */
    public void init(String imagePath) {
        if (TextUtils.isEmpty(imagePath))
            return;
        RoundImageView bgIV = new RoundImageView(getContext());
        int width = (int) (mWidth * 0.78);
        LayoutParams layoutParams = new LayoutParams((int) (width * 0.8), width);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        bgIV.setBorderRadius(DipPxConversion.dip2px(getContext(), 3));
        bgIV.setLayoutParams(layoutParams);
        bgIV.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        addView(bgIV);
        //
        drawText();

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
