package com.storyshu.storyshu.widget.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.DipPxConversion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 标志view
 * Created by bear on 2016/12/2.
 */

public class PersonView extends View {
    private float mCenterX = 0; //水平的中心点
    private float mCenterY = 0; //水平的中心点
    private int mWidth; //圆形图片宽度,直径
    private float mFrameWidth; //白色的背景边框宽度
    private int mFrameColor; //边框颜色
    private int mShadowColor; //阴影颜色
    private int mHitColor; //选中的颜色
    private int mDefColor; //未选中的颜色

    private Paint mPaint; //画笔
    private Paint mShaderPaint; //用于绘制头像

    private Matrix mMatrix; //缩放矩阵
    private BitmapShader mBmpShader; //渲染图像
    private Bitmap mAvatarBmp; //头像bitmap
    private String mImageUrl; //图片的地址


    /**
     * 显示头像图片
     *
     * @param bmp
     */
    public void init(Bitmap bmp) {
        if (bmp == null)
            return;

        mAvatarBmp = Bitmap.createScaledBitmap(bmp, mWidth, mWidth, true);
        bmp.recycle();

        requestLayout();
    }

    public void init(GlideBitmapDrawable bmp) {
        if (bmp == null)
            return;
        mAvatarBmp = bmp.getBitmap();

        requestLayout();
    }

    /**
     * 加载本地头像图片
     *
     * @param imagePath
     */
    public void init(String imagePath) {
        if (TextUtils.isEmpty(imagePath))
            return;

        mAvatarBmp = getLocalBitmap(imagePath);
        requestLayout();
    }

    /**
     * 设置为选中的状态
     */
    public void setSelectedMode() {
        mWidth = (int) getResources().getDimension(R.dimen.image_big);
        requestLayout();

        mFrameColor = mHitColor;
        postInvalidate();
    }

    public PersonView(Context context) {
        this(context, null);
    }

    public PersonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PersonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mShaderPaint = new Paint();
        mShaderPaint.setAntiAlias(true);
        mShaderPaint.setStyle(Paint.Style.FILL);

        mMatrix = new Matrix();

        mFrameWidth = DipPxConversion.dip2px(getContext(), 4);
        mAvatarBmp = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_wolverine);

        mHitColor = getResources().getColor(R.color.colorOrange);
        mDefColor = getResources().getColor(R.color.colorWhite);
        mFrameColor = mDefColor;
        mShadowColor = getResources().getColor(R.color.colorGrayLight);
        mWidth = (int) getResources().getDimension(R.dimen.image_normal);
    }

    /**
     * 背景
     */
    private void drawBg(Canvas canvas) {
        mPaint.setShader(null);
        //画阴影
        mPaint.setColor(mShadowColor);
        canvas.drawCircle(mCenterX, mCenterY, mCenterX - 1, mPaint);

        //画白色背景
        mPaint.setColor(mFrameColor);
        canvas.drawCircle(mCenterX, mCenterY, mCenterX - 2, mPaint);
    }

    /**
     * 画坐标点
     */
    private void drawPoint(Canvas canvas) {
        mPaint.setColor(mShadowColor);
        canvas.drawCircle(mCenterX, getHeight() - mFrameWidth, mFrameWidth * 0.7f, mPaint);

        mPaint.setColor(mHitColor);
        canvas.drawCircle(mCenterX, getHeight() - mFrameWidth, mFrameWidth * 0.7f
                - DipPxConversion.dip2px(getContext(), 1), mPaint);
    }

    /**
     * 设置着色器
     */
    private void setUpShader(Bitmap bmp) {
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBmpShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale;
        int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
        scale = mWidth * 1.0f / bSize;

        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBmpShader.setLocalMatrix(mMatrix);
        // 设置shader
        mShaderPaint.setShader(mBmpShader);
    }


    /**
     * 头像
     */
    private void drawAvatar(Canvas canvas) {
        if (mAvatarBmp == null)
            return;

        //萃取颜色
        Palette palette = Palette.from(mAvatarBmp).generate();
        mPaint.setColor(palette.getLightMutedColor(mDefColor));
        canvas.drawCircle(mCenterX, mCenterY, (mWidth - mFrameWidth) / 2, mPaint);

        //画头像
        setUpShader(mAvatarBmp);
        canvas.drawCircle(mCenterX, mCenterY, (mWidth - mFrameWidth) / 2, mShaderPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCenterX == 0) {
            mWidth = Math.min(getWidth(), getHeight());

            mCenterX = mWidth / 2;
            mCenterY = mWidth / 2;
        }

        drawBg(canvas);

        drawPoint(canvas);

        drawAvatar(canvas);
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public Bitmap getLocalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mWidth == 0)
            mWidth = (int) getResources().getDimension(R.dimen.image_normal);
        setMeasuredDimension(mWidth, (int) (mWidth * 1.22f));
    }
}
