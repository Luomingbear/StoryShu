package com.storyshu.storyshu.widget.blurRelativeLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.BitmapUtil;

/**
 * 背景模糊的相对布局
 * Created by bear on 2017/1/27.
 */

public class BlurRelativeLayout extends RelativeLayout {
    private int mBlurBitmapRes;
    private ImageView mBlurImageView;

    public BlurRelativeLayout(Context context) {
        this(context, null);
    }

    public BlurRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlurRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BlurRelativeLayout);
        mBlurBitmapRes = typedArray.getResourceId(R.styleable.BlurRelativeLayout_blurDrawable, -1);
        typedArray.recycle();
        init();
    }

    private void init() {
        mBlurImageView = new ImageView(getContext());
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mBlurImageView.setLayoutParams(p);
        mBlurImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(mBlurImageView);

        //添加黑色半透明view
        View blackView = new View(getContext());
        blackView.setLayoutParams(p);
        blackView.setBackgroundColor(getResources().getColor(R.color.colorTranslateDark));
        addView(blackView);

        if (mBlurBitmapRes == -1)
            return;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mBlurBitmapRes);
        if (bitmap != null) {
            setBlurBitmap(bitmap);
        }
    }

    /**
     * 设置模糊的背景，一次模糊
     *
     * @param bmp
     */
    public void setBlurBitmap(Bitmap bmp) {
        if (bmp == null)
            return;
        try {
            bmp = BitmapUtil.centerSquareScaleBitmap(bmp, 100);
            BitmapDrawable drawable = new BitmapDrawable(bmp);
            setBackgroundDrawable(drawable);
            Bitmap blurbmp1 = BitmapUtil.rsBlurBitmap(bmp,
                    getContext(), 25);
            mBlurImageView.setImageBitmap(blurbmp1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置模糊的背景，两次模糊
     *
     * @param bmp
     */
    public void setBlur2Bitmap(Bitmap bmp) {
        if (bmp == null)
            return;
        try {
            bmp = BitmapUtil.centerSquareScaleBitmap(bmp, 100);
            BitmapDrawable drawable = new BitmapDrawable(bmp);
            setBackgroundDrawable(drawable);
            Bitmap blurBmp1 = BitmapUtil.rsBlurBitmap(bmp, getContext(), 25);
            Bitmap blurBmp2 = BitmapUtil.rsBlurBitmap(blurBmp1, getContext(), 25);
            mBlurImageView.setImageBitmap(blurBmp2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置高斯模糊的背景显示，可以实时调节
     *
     * @param ratio 0~1
     */
    public void setBlurRatio(float ratio) {
        if (mBlurImageView == null)
            return;

        mBlurImageView.setAlpha(ratio);
    }


    /**
     * 设置模糊的背景
     * 加载本地图片
     *
     * @param blurImgPath
     */
    public void setLocalBlurBitmap(String blurImgPath) {
        try {
            setBlurBitmap(BitmapFactory.decodeFile(blurImgPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置模糊的背景
     * 加载网络图片
     *
     * @param blurImgPath
     */
    public void setNetBlurBitmap(String blurImgPath) {
        try {
            Glide.with(getContext()).load(blurImgPath).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    setBlurBitmap(BitmapUtil.drawable2Bitamp(resource));
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
