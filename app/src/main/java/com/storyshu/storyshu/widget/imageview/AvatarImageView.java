package com.storyshu.storyshu.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.DipPxConversion;

/**
 * 匿名的头像
 * 添加一个背景色
 * Created by bear on 2017/3/14.
 */

public class AvatarImageView extends AppCompatImageView {
    /**
     * 图片的类型，圆形、圆角、指定角圆角
     */
    private int type;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_BORDER = TYPE_CIRCLE + 1; //加描边

    /**
     * 宽高比
     */
    float scale = 0f;
    /**
     * 绘图的Paint
     */
    private Paint mBitmapPaint;
    private Paint mBgPaint;
    /**
     * 圆角的半径
     */
    private int mRadius;
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;
    /**
     * view的宽度
     */
    private int mWidth;
    private RectF mRoundRect;

    public AvatarImageView(Context context, AttributeSet attrs) {

        super(context, attrs);
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView);
        if (a != null) {
            type = a.getInt(R.styleable.AvatarImageView_avatarType, TYPE_CIRCLE);
            a.recycle();
        }
    }

    public AvatarImageView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (scale > 0) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
            // Children are just made to fill our space.
            int childWidthSize = getMeasuredWidth();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize / scale), MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */

        mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = mWidth / 2;
        setMeasuredDimension(mWidth, mWidth);


    }

    public void setScale(float scale) {
        if (scale > 0)
            this.scale = scale;
        requestLayout();
    }

    /**
     * 初始化BitmapShader
     */
    private void setUpShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        Bitmap bmp = drawableToBitmap(drawable);
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        // 拿到bitmap宽或高的小值
        int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
        scale = mWidth * 1.0f / bSize;

        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mBitmapPaint.setShader(mBitmapShader);

        //获取高亮颜色
        Palette palette = Palette.from(bmp).generate();
        mBgPaint.setColor(palette.getLightMutedColor(getResources().getColor(R.color.colorWhite)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        setUpShader();
        switch (type) {
            case TYPE_CIRCLE:
                canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
                break;

            case TYPE_BORDER:
                mBgPaint.setColor(Color.WHITE);
                canvas.drawCircle(mRadius, mRadius, mRadius, mBgPaint);
                canvas.drawCircle(mRadius, mRadius, mRadius - DipPxConversion.dip2px(getContext(), 2), mBitmapPaint);
                break;
        }


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 圆角图片的范围
        if (type != TYPE_CIRCLE)
            mRoundRect = new RectF(0, 0, w, h);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_TYPE, type);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state).getParcelable(STATE_INSTANCE));
            this.type = bundle.getInt(STATE_TYPE);
        } else {
            super.onRestoreInstanceState(state);
        }

    }

    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
        }
        requestLayout();
    }


}
