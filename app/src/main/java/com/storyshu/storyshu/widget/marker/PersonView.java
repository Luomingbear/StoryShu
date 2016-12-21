package com.storyshu.storyshu.widget.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.imageview.RoundImageView;

/**
 * 标志view
 * Created by bear on 2016/12/2.
 */

public class PersonView extends RelativeLayout {
    private int mWidth; //宽度
    private String mImageUrl; //图片的地址
    private RoundImageView mHeadPicIV;

    public PersonView(Context context) {
        this(context, null);
    }

    public PersonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PersonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWidth = (int) getResources().getDimension(R.dimen.icon_large);

        drawBg();
        drawHeadPic();
    }

    public void init(Bitmap bmp) {
        if (bmp == null)
            return;
        mHeadPicIV.setImageBitmap(bmp);
//        invalidate();
    }

    /**
     * 背景
     */
    private void drawBg() {
        ImageView bgIV = new ImageView(getContext());
        LayoutParams p = new LayoutParams(mWidth, mWidth);
//        p.addRule(RelativeLayout.CENTER_HORIZONTAL);
        bgIV.setLayoutParams(p);
        bgIV.setBackgroundResource(R.drawable.location1);
        addView(bgIV);
    }

    /**
     * 头像
     */
    private void drawHeadPic() {
        mHeadPicIV = new RoundImageView(getContext());
        float width = mWidth * 0.78f;
        LayoutParams layoutParams = new LayoutParams((int) width, (int) width);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mHeadPicIV.setLayoutParams(layoutParams);
        mHeadPicIV.setType(RoundImageView.TYPE_CIRCLE);
//        mHeadPicIV.setBackgroundResource(R.drawable.person_location);

        addView(mHeadPicIV);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = (int) getResources().getDimension(R.dimen.icon_large);
        setMeasuredDimension(mWidth, mWidth);
    }
}
