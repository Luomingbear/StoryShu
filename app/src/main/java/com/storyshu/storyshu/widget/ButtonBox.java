package com.storyshu.storyshu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.ButtonBoxInfo;
import com.storyshu.storyshu.widget.text.RoundTextView;

/**
 * 实现选择功能的按钮，类似checkbox
 * Created by bear on 2017/7/8.
 */

public class ButtonBox extends RelativeLayout {
    private ButtonBoxInfo boxInfo;
    private float mCBSize; //图标的大小
    private float mTextSize; //文字的大小
    private TextView mTitleTv;
    private RoundTextView mNumTv;
    private ImageView mDrawbaleIv;
    private boolean isSelect = false;

    private int mUnreadNum = 0;

    public ButtonBox(Context context, ButtonBoxInfo buttonBoxInfo) {
        super(context);
        this.boxInfo = buttonBoxInfo;

        init();
    }

    public ButtonBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mTextSize = getResources().getDimension(R.dimen.font_small);

        mCBSize = getResources().getDimension(R.dimen.icon_small);

        if (boxInfo != null) {

            newButtonLayout();
        }

    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;

        if (mDrawbaleIv != null && mTitleTv != null)
            if (isSelect) {
                mDrawbaleIv.setImageResource(boxInfo.getDrawableResSel());
                mTitleTv.setTextColor(boxInfo.getColorSel());
            } else {
                mDrawbaleIv.setImageResource(boxInfo.getDrawableResDef());
                mTitleTv.setTextColor(boxInfo.getColorDef());
            }
    }

    public int getUnreadNum() {
        return mUnreadNum;
    }

    public void setUnreadNum(int unreadNum) {
        this.mUnreadNum = unreadNum;
        if (unreadNum != 0) {
            mNumTv.setVisibility(VISIBLE);
            mNumTv.setText(mUnreadNum + "");
        } else {
            mNumTv.setVisibility(GONE);
        }

        postInvalidate();
    }

    public ButtonBoxInfo getBoxInfo() {
        return boxInfo;
    }

    public void setBoxInfo(ButtonBoxInfo boxInfo) {
        this.boxInfo = boxInfo;
        removeAllViewsInLayout();

        init();
    }

    /**
     * 生成一个按钮
     *
     * @return
     */
    private void newButtonLayout() {
        newTitle();

        newIv();

        LinearLayout linearLayout = new LinearLayout(getContext());
        LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT);
        linearLayout.setLayoutParams(params1);

        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(mDrawbaleIv);
        linearLayout.addView(mTitleTv);
        linearLayout.setId(R.id.content_layout);

        newNumText();

        addView(linearLayout);
        addView(mNumTv);

    }

    /**
     * 生成文本view
     *
     * @return
     */
    private void newTitle() {
        mTitleTv = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mTitleTv.setLayoutParams(params);

        mTitleTv.setText(boxInfo.getTitleRes());
        mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mTitleTv.setTextColor(boxInfo.getColorDef());
    }

    /**
     * 生成新的checkbox
     *
     * @return
     */
    private void newIv() {
        LayoutParams params = new LayoutParams((int) mCBSize, (int) mCBSize);
        params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.margin_min));


        mDrawbaleIv = new ImageView(getContext());
        mDrawbaleIv.setLayoutParams(params);
        mDrawbaleIv.setImageResource(boxInfo.getDrawableResDef());
        mDrawbaleIv.setId(mDrawbaleIv.hashCode());

    }

    private void newNumText() {
        mNumTv = new RoundTextView(getContext());
        LayoutParams params = new LayoutParams((int) getResources().getDimension(R.dimen.icon_min),
                (int) getResources().getDimension(R.dimen.icon_min));
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.content_layout);
        mNumTv.setLayoutParams(params);

        mNumTv.setBgColor(R.color.colorRed);
        mNumTv.setRoundSize(getResources().getDimension(R.dimen.margin_small));
        mNumTv.setGravity(Gravity.CENTER);
        mNumTv.setVisibility(INVISIBLE); //默认不可见
        mNumTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_small));
        mNumTv.setTextColor(getResources().getColor(R.color.colorWhite));
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (isSelect)
//                    mDrawbaleIv.setImageResource(boxInfo.getDrawableResSel());
//                else
//                    mDrawbaleIv.setImageResource(boxInfo.getDrawableResDef());
//                break;
//
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//
//
//        return true;
//
//    }


}
