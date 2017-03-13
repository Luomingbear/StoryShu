package com.storyshu.storyshu.widget.blurRelativeLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;

/**
 * 底部的导航栏
 * Created by bear on 2017/3/13.
 */

public class BottomNavigationBar extends LinearLayout {
    private static final String TAG = "BottomNavigationBar";
    private float mTextSize; //文字的大小
    private int mTextColorDef; //默认文字的颜色
    private int mTextColorHit; //选中的文字的颜色
    private float mCBSize; //图标的大小

    private int num = 4;
    private CheckBox[] checkBoxes = new CheckBox[num]; // 图标集合

    private int[] cBDrwables = new int[num]; //图标背景集合

    private TextView[] textViews = new TextView[num]; //文字集合

    private int[] textStrings = new int[num]; //文本内容集合

    private OnBottomNavigationClickListener listener;

    public void setNavigationClickListener(OnBottomNavigationClickListener listener) {
        this.listener = listener;
    }

    /**
     * 底部导航栏的点击回调
     */
    public interface OnBottomNavigationClickListener {
        void onClick(int position);
    }

    public BottomNavigationBar(Context context) {
        this(context, null);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

        initView();
        setClick(0);
    }

    /**
     * 初始化数据
     */
    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.BOTTOM);

        mTextSize = getResources().getDimension(R.dimen.font_min);
        mTextColorDef = getResources().getColor(R.color.colorGrayLight);
        mTextColorHit = getResources().getColor(R.color.colorGoldLight);

        mCBSize = getResources().getDimension(R.dimen.icon_small);

        //初始化
        for (int i = 0; i < num; i++) {
            checkBoxes[i] = new CheckBox(getContext());
            textViews[i] = new TextView(getContext());
        }

        cBDrwables[0] = R.drawable.button_story_map;
        cBDrwables[1] = R.drawable.button_airport;
        cBDrwables[2] = R.drawable.button_message;
        cBDrwables[3] = R.drawable.button_mine;

        textStrings[0] = R.string.story_map;
        textStrings[1] = R.string.airport;
        textStrings[2] = R.string.message;
        textStrings[3] = R.string.mine;
    }

    /**
     * 设置点击之后的显示效果
     *
     * @param position
     */
    public void setClick(int position) {
        if (listener != null)
            listener.onClick(position);

        for (int i = 0; i < num; i++) {
            if (i == position) {
                if (!checkBoxes[i].isChecked()) {
                    checkBoxes[i].setChecked(true);
                    textViews[i].setTextColor(mTextColorHit);
                }
            } else {
                if (checkBoxes[i].isChecked()) {
                    checkBoxes[i].setChecked(false);
                    textViews[i].setTextColor(mTextColorDef);
                }
            }
        }

    }

    private void addCreateButton() {
        View view = new View(getContext());
        LayoutParams params = new LayoutParams((int) getResources().getDimension(R.dimen.navigation_height),
                LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);

        addView(view);
    }

    /**
     * 初始化view,设置控件
     */
    private void initView() {
        //添加按钮
        LinearLayout oneLayout = newButtonLayout(textViews[0], checkBoxes[0], textStrings[0], cBDrwables[0]);
        addView(oneLayout);
        oneLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setClick(0);
            }
        });

        LinearLayout twoLayout = newButtonLayout(textViews[1], checkBoxes[1], textStrings[1], cBDrwables[1]);
        addView(twoLayout);
        twoLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setClick(1);
            }
        });

        //添加写故事按钮
        addCreateButton();

        //
        LinearLayout threeLayout = newButtonLayout(textViews[2], checkBoxes[2], textStrings[2], cBDrwables[2]);
        addView(threeLayout);
        threeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setClick(2);
            }
        });

        LinearLayout fourLayout = newButtonLayout(textViews[3], checkBoxes[3], textStrings[3], cBDrwables[3]);
        addView(fourLayout);
        fourLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setClick(3);
            }
        });
    }

    /**
     * 生成一个按钮
     *
     * @param tv  textView
     * @param cb  Checkbox
     * @param tvt textView 的文本
     * @param cbb checkbox 的图标背景
     * @return
     */
    private LinearLayout newButtonLayout(TextView tv, CheckBox cb, int tvt, int cbb) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(0, (int) getResources().getDimension(R.dimen.title_height), 1);
        linearLayout.setLayoutParams(params);

        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(VERTICAL);

        cb = newCB(cb, cbb);
        tv = newText(tv, tvt);

        linearLayout.addView(cb);
        linearLayout.addView(tv);

        return linearLayout;
    }

    /**
     * 生成文本view
     *
     * @param navigation 默认文本
     * @return
     */
    private TextView newText(TextView textView, int navigation) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);

        textView.setText(navigation);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        textView.setTextColor(mTextColorDef);
        return textView;
    }

    /**
     * 生成新的checkbox
     *
     * @param drawableRes 背景图标
     * @return
     */
    private CheckBox newCB(CheckBox checkBox, int drawableRes) {
        LayoutParams params = new LayoutParams((int) mCBSize, (int) mCBSize);
        params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.margin_min));
        checkBox.setLayoutParams(params);


        checkBox.setBackgroundResource(drawableRes);
        checkBox.setButtonDrawable(null);
        checkBox.setClickable(false);
        return checkBox;
    }

}
