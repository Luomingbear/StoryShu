package com.storyshu.storyshu.widget.marker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.storyshu.storyshu.R;

/**
 * 故事集图标的view
 * Created by bear on 2017/2/14.
 */

public class IBookView extends View {
    private Paint mPaint;

    private int mWidth; //图标的宽度

    private int mCoverColor; //扉页的颜色 有金、银、铜、普通

    private int mTitleColor; //标题的颜色

    private BookLevel mBookLevel = BookLevel.NORMAL; //故事集的等级,默认普通

    private String mTitle; //故事集名字

    private Bitmap mCoverBmp; //封面图片

    private RectF mRectF; //图片的显示区域

    /**
     * 宽高比
     */
    float scale = 0f;

    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;

    /**
     * 故事集的类型、等级
     */
    private enum BookLevel {
        //普通
        NORMAL,

        //铜
        COPPER,

        //银
        SILVER,

        //黄金
        GOLD,


    }

    public IBookView(Context context) {
        this(context, null);
    }

    public IBookView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IBookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IBookView);
        if (typedArray != null) {
            mBookLevel = BookLevel.values()[typedArray.
                    getInt(R.styleable.IBookView_BookLevel, 0)];
            typedArray.recycle();
        }
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mMatrix = new Matrix();

        mCoverBmp = BitmapFactory.decodeResource(getResources(), R.drawable.image);

        //扉页的颜色
        mCoverColor = getResources().getColor(R.color.colorNormal);
        switch (mBookLevel) {
            case COPPER:
                mCoverColor = getResources().getColor(R.color.colorCopper);
                break;
            case GOLD:
                mCoverColor = getResources().getColor(R.color.colorGold);
                break;
            case NORMAL:
                mCoverColor = getResources().getColor(R.color.colorNormal);
                break;
            case SILVER:
                mCoverColor = getResources().getColor(R.color.colorSilver);
                break;
        }


        //名字的颜色
        mTitleColor = getResources().getColor(R.color.colorBlack);

        mTitle = getResources().getString(R.string.app_name);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = (int) getResources().getDimension(R.dimen.icon_large);
        setMeasuredDimension(width, (int) (width * 1.8f));
    }

    private void drawBgShadow(Canvas canvas) {
        int bgColor = getResources().getColor(R.color.colorGrayLight);

        RectF rectF = new RectF(0, 0, getWidth(), mWidth * 1.5f);

        mPaint.setColor(bgColor);
        canvas.drawRect(rectF, mPaint);

        /**
         * 添加尖角
         */
        Bitmap jianjiaoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.jianjiao_bg);

        Rect rectDst = new Rect(0, (int) (mWidth * 1.5f), getWidth(), getHeight());
        Rect rectSrc = new Rect(0, 0, jianjiaoBmp.getWidth(), jianjiaoBmp.getHeight());
//        BitmapShader shader = new BitmapShader(jianjiaoBmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//
//        //获取图片的缩放比例
//        scale = mWidth * 1.0f / jianjiaoBmp.getWidth();
//
//        //设置缩放
//        mMatrix.setScale(scale, scale);
//        shader.setLocalMatrix(mMatrix);
//        mPaint.setShader(shader);
//        canvas.drawRect(rectF1, mPaint);
        canvas.drawBitmap(jianjiaoBmp, rectSrc, rectDst, mPaint);

    }

    /**
     * 设置显示的shader
     */
    private void setUpBmpShader() {
        mRectF = new RectF(0, 0, mWidth, mWidth);

        //设置显示的图片
        mBitmapShader = new BitmapShader(mCoverBmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //获取图片的缩放比例
        if (mCoverBmp != null) {
            scale = mWidth * 1.0f / Math.min(mCoverBmp.getWidth(), mCoverBmp.getHeight());
        }

        //设置缩放
        mMatrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        mPaint.setShader(mBitmapShader);
    }

    /**
     * 绘制扉页
     * 填充背景颜色
     */
    private void drawTitlePage(Canvas canvas) {
        RectF rectF = new RectF(0, mWidth, mWidth, mWidth * 1.5f);
        mPaint.setShader(null);
        mPaint.setColor(mCoverColor);
        canvas.drawRect(rectF, mPaint);
    }

    /**
     * 绘制故事集的名字
     *
     * @param canvas
     */
    private void drawTitle(Canvas canvas) {
        mPaint.setColor(mTitleColor);
        mPaint.setTextSize(getResources().getDimension(R.dimen.font_min));
        //字符串的长度
        float w = mPaint.measureText(mTitle);
        w = Math.min(getWidth(), w);

        float x = (getWidth() - w) / 2;
        float y = (getHeight() + mWidth) / 2;

        canvas.drawText(mTitle, x, y, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = (int) (getWidth() * 0.95f);


        drawBgShadow(canvas);

        //绘制封面图
        setUpBmpShader();
        canvas.drawRect(mRectF, mPaint);


        /**
         * 扉页
         */
        drawTitlePage(canvas);

        /**
         * 故事集名字
         */
//        drawTitle(canvas);

    }

    /**
     * 设置封面图片
     *
     * @param coverBmp
     */
    public void setCoverBmp(Bitmap coverBmp) {
        this.mCoverBmp = coverBmp;
        requestLayout();
    }

    /**
     * 显示封面图片
     *
     * @param localPath 本地路径
     */
    public void setCoverBmp(String localPath) {
        if (TextUtils.isEmpty(localPath))
            return;

        mCoverBmp = BitmapFactory.decodeFile(localPath);
        requestLayout();
    }

    public void setTitle(String title) {
        this.mTitle = title;
        requestLayout();
    }

    public void setTitleColor(int titleColor) {
        this.mTitleColor = titleColor;
        requestLayout();
    }
}
