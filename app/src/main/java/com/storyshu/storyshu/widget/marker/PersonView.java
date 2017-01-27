package com.storyshu.storyshu.widget.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.imageview.RoundImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
        drawAvatar();
    }

    public void init(Bitmap bmp) {
        if (bmp == null)
            return;
        mHeadPicIV.setImageBitmap(bmp);
    }

    public void init(String imagePath) {
        if (TextUtils.isEmpty(imagePath))
            return;
        mHeadPicIV.setImageBitmap(getLocalBitmap(imagePath));
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLocalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 背景
     */
    private void drawBg() {
        ImageView bgIV = new ImageView(getContext());
        LayoutParams p = new LayoutParams(mWidth, mWidth);
        p.addRule(RelativeLayout.CENTER_HORIZONTAL);
        bgIV.setLayoutParams(p);
        bgIV.setBackgroundResource(R.drawable.person_marker_bg);
        addView(bgIV);
    }

    /**
     * 头像
     */
    private void drawAvatar() {
        mHeadPicIV = new RoundImageView(getContext());
        float width = mWidth * 0.78f;
        LayoutParams layoutParams = new LayoutParams((int) width, (int) width);
        int margin = (int) (mWidth * 0.1f);
        layoutParams.setMargins(margin, 0, margin, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mHeadPicIV.setLayoutParams(layoutParams);
        mHeadPicIV.setType(RoundImageView.TYPE_CIRCLE);

        addView(mHeadPicIV);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        mWidth = (int) getResources().getDimension(R.dimen.icon_large);
//        setMeasuredDimension(mWidth, mWidth);
        super.onMeasure(mWidth, mWidth);

    }
}
