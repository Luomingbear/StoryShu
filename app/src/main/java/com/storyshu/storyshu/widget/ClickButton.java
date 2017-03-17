package com.storyshu.storyshu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;

/**
 * 点赞、评论、喝倒彩的按钮
 * Created by bear on 2017/3/15.
 */

public class ClickButton extends LinearLayout {
    private static final String TAG = "ClickButton";
    private Type mType; //按钮样式
    private CGravity mGravity; //图标的位置
    private int mDrawableRes; //图标的id
    private int mDrawableWidth; //图标的宽度
    private int mTextSize; //文字的大小
    private int margin; //外边距
    private int mTextColor;
    private String mString; //数量

    private CheckBox mDrawableCb; //图标的控件
    private TextView mTextView; //文本的控件

    private boolean isClicked = false;
    private OnClickButtonListener onClickButtonLisener;

    /**
     * 点击回调接口
     */
    public interface OnClickButtonListener {
        void onCLicked(boolean isClicked);
    }

    /**
     * 设置点击回调接口
     *
     * @param onClickButtonLisener
     */
    public void setOnClickButtonListener(OnClickButtonListener onClickButtonLisener) {
        this.onClickButtonLisener = onClickButtonLisener;
    }

    /**
     * 设置数值
     *
     * @param num
     */
    public void setNum(int num) {
        if (num > 999)
            mString = "999+";
        else
            mString = num + "";
        mTextView.setText(mString);
    }

    /**
     * 获取点击的状态
     *
     * @return
     */
    public boolean isClicked() {
        return isClicked;
    }

    /**
     * 设置点击状态
     *
     * @param clicked
     */
    public void setClicked(boolean clicked) {
        isClicked = clicked;
        mDrawableCb.setChecked(isClicked);

        if (onClickButtonLisener != null)
            onClickButtonLisener.onCLicked(isClicked);
    }

    /**
     * 设置点击状态
     */
    public void setClicked() {
        setClicked(!isClicked);
    }


    public enum Type {
        LIKE,

        OPPOSE,

        COMMENT
    }

    public enum CGravity {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }

    public ClickButton(Context context) {
        this(context, null);
    }

    public ClickButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClickButton);
        mDrawableWidth = (int) typedArray.getDimension(R.styleable.ClickButton_cDrawableWidth,
                getResources().getDimension(R.dimen.icon_small));
        mString = typedArray.getString(R.styleable.ClickButton_cTextString);
        mTextSize = (int) typedArray.getDimension(R.styleable.ClickButton_cTextSize,
                getResources().getDimension(R.dimen.font_min));
        mGravity = CGravity.values()[typedArray.getInt(R.styleable.ClickButton_cGravity, 0)];
        mType = Type.values()[typedArray.getInt(R.styleable.ClickButton_cType, 0)];
        typedArray.recycle();

        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);

        margin = (int) getResources().getDimension(R.dimen.margin_min);
        if (TextUtils.isEmpty(mString))
            mString = "0";

        /**
         * 设置背景资源
         */
        switch (mType) {
            case COMMENT:
                mDrawableRes = R.drawable.button_comment;
                mTextColor = getResources().getColor(R.color.colorGray);
                break;
            case LIKE:
                mDrawableRes = R.drawable.button_like;
                mTextColor = getResources().getColor(R.color.colorRedLight);
                break;
            case OPPOSE:
                mDrawableRes = R.drawable.button_oppose;
                mTextColor = getResources().getColor(R.color.colorGoldLight);
                break;
        }
        mDrawableWidth = (int) getResources().getDimension(R.dimen.icon_min);
        mTextSize = (int) (mDrawableWidth * 0.8f);

        setCB();
        setTV();
        initLayout();
    }

    private void setCB() {
        mDrawableCb = new CheckBox(getContext());
        LayoutParams params = new LayoutParams(mDrawableWidth, mDrawableWidth);
        mDrawableCb.setLayoutParams(params);

        mDrawableCb.setButtonDrawable(null);
        mDrawableCb.setBackgroundResource(mDrawableRes);
//      mDrawableCb.setClickable(true);
    }

    private void setTV() {
        mTextView = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, margin, margin, margin);
        mTextView.setLayoutParams(params);

        mTextView.setTextColor(mTextColor);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mTextView.setText(mString);
    }

    /**
     * 布局
     */
    private void initLayout() {
        removeAllViewsInLayout();

        switch (mGravity) {
            case LEFT:
                setOrientation(HORIZONTAL);
                addView(mDrawableCb);
                addView(mTextView);
                break;

            case RIGHT:
                setOrientation(HORIZONTAL);
                addView(mTextView);
                addView(mDrawableCb);
                break;

            case TOP:
                setOrientation(VERTICAL);
                addView(mDrawableCb);
                addView(mTextView);
                break;

            case BOTTOM:
                setOrientation(VERTICAL);
                addView(mTextView);
                addView(mDrawableCb);
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        switch (mGravity) {
            case LEFT:
            case RIGHT:
                setMeasuredDimension(margin * 2 + mDrawableWidth + mTextView.getMeasuredWidth(),
                        mDrawableWidth);
                break;
            case TOP:
            case BOTTOM:
                setMeasuredDimension(Math.max(mTextView.getMeasuredWidth() + margin * 2, mDrawableWidth),
                        mDrawableWidth + mTextView.getMeasuredHeight() + margin * 2);
                break;
        }
    }


    private float downX, downY; //手指按下的坐标

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                if (downX > 0 && downX < getWidth())
                    if (downY > 0 && downY < getHeight()) {
                        if (mType == Type.COMMENT)
                            mDrawableCb.setBackgroundResource(R.drawable.comment_on);
                    }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                if (mType == Type.COMMENT)
                    mDrawableCb.setBackgroundResource(R.drawable.comment);

                float upX, upY;
                upX = event.getX();
                upY = event.getY();
                if (upX > 0 && upX < getWidth())
                    if (upY > 0 && upY < getHeight()) {
                        setClicked();
                    }
                break;
        }

        return true;

    }
}
