package com.storyshu.storyshu.widget.blurRelativeLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.BitmapUtil;

/**
 * 背景模糊的相对布局
 * Created by bear on 2017/1/27.
 */

public class BlurRelativeLayout extends FrameLayout {
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
            Bitmap blurbmp1 = BitmapUtil.blurBitmap(bmp,
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
            Bitmap blurBmp1 = BitmapUtil.blurBitmap(bmp, getContext(), 25);
            Bitmap blurBmp2 = BitmapUtil.blurBitmap(blurBmp1, getContext(), 25);
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
            ImageLoader.getInstance().loadImage(blurImgPath, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    setBlurBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
