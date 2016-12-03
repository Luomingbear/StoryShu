package com.bear.passby.widget.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bear.passby.R;

/**
 * 标题栏
 * Created by bear on 2016/12/2.
 */

public class TitleView extends RelativeLayout {
    private int mTitleViewHeight; //控件的高度
    private int mIconWidth; // 图标的宽度
    private TextView mTitleTextView; //标题文本
    private int mTitleCoclor; // 标题栏字体颜色
    private float mTitleSize; // 标题栏字体大小
    private String mTitleString; //标题栏文本

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
        mTitleCoclor = typedArray.getColor(R.styleable.TitleView_title_color, Color.BLACK);
        mTitleSize = typedArray.getDimension(R.styleable.TitleView_title_size, getResources().getDimension(R.dimen.font_normal));
        mTitleString = typedArray.getString(R.styleable.TitleView_title_string);
        typedArray.recycle();
        init();
    }

    private void init() {

        mIconWidth = (int) getResources().getDimension(R.dimen.icon_small);
        mTitleViewHeight = (int) getResources().getDimension(R.dimen.title_height);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundResource(R.color.colorWhite);
        removeAllViews();

        addLeftButton();

        addTitle();

        addRightButton();
    }

    private void addLeftButton() {
        addView(newImageButton(RelativeLayout.ALIGN_PARENT_LEFT, R.drawable.meun));
    }

    private void addTitle() {
        if (TextUtils.isEmpty(mTitleString))
            return;
        mTitleTextView = new TextView(getContext());
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        p.addRule(CENTER_IN_PARENT);
        mTitleTextView.setLayoutParams(p);
        mTitleTextView.setText(mTitleString);
        mTitleTextView.setTextColor(mTitleCoclor);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);

        addView(mTitleTextView);
    }

    private void addRightButton() {
        addView(newImageButton(RelativeLayout.ALIGN_PARENT_RIGHT, R.drawable.sift));

    }

    /**
     * 添加图标的饿按钮
     *
     * @param gravity 位置
     * @param resId   图标资源id
     */
    private RelativeLayout newImageButton(int gravity, int resId) {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        LayoutParams p = new LayoutParams(mTitleViewHeight, mTitleViewHeight);
        p.addRule(gravity);
        relativeLayout.setLayoutParams(p);
        relativeLayout.setGravity(Gravity.CENTER);

        relativeLayout.addView(newImageView(resId));
        relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return relativeLayout;
    }


    private ImageView newImageView(int resId) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(resId);

        LayoutParams p = new LayoutParams(mIconWidth, mIconWidth);
        imageView.setLayoutParams(p);
        return imageView;
    }


}
