package com.storyshu.storyshu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.ButtonBoxInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部的导航栏
 * Created by bear on 2017/3/13.
 */

public class BottomNavigationBar extends LinearLayout {
    private static final String TAG = "BottomNavigationBar";
    private int mTextColorDef; //默认文字的颜色
    private int mTextColorHit; //选中的文字的颜色

    private int num = 4;
    private List<ButtonBoxInfo> mBoxInfoList; // 数据
    private List<ButtonBox> mButtonList; // 图标集合

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

        mTextColorDef = getResources().getColor(R.color.colorGray);
        mTextColorHit = getResources().getColor(R.color.colorRed);


        //初始化
        mBoxInfoList = new ArrayList<>();
        ButtonBoxInfo buttonInfo1 = new ButtonBoxInfo(R.drawable.unmap,
                R.drawable.map, mTextColorDef, mTextColorHit, R.string.story_map);
        mBoxInfoList.add(buttonInfo1);

        ButtonBoxInfo buttonInfo2 = new ButtonBoxInfo(R.drawable.unairport,
                R.drawable.airport, mTextColorDef, mTextColorHit, R.string.airport);
        mBoxInfoList.add(buttonInfo2);


        ButtonBoxInfo buttonInfo3 = new ButtonBoxInfo(R.drawable.unmessage,
                R.drawable.message, mTextColorDef, mTextColorHit, R.string.message);
        mBoxInfoList.add(buttonInfo3);

        ButtonBoxInfo buttonInfo4 = new ButtonBoxInfo(R.drawable.unme,
                R.drawable.me, mTextColorDef, mTextColorHit, R.string.mine);
        mBoxInfoList.add(buttonInfo4);

        mButtonList = new ArrayList<>();

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
                if (!mBoxInfoList.get(i).isChecked()) {
                    mBoxInfoList.get(i).setChecked(true);
                    mButtonList.get(i).setSelect(true);
                }
            } else {
                if (mBoxInfoList.get(i).isChecked()) {
                    mBoxInfoList.get(i).setChecked(false);
                    mButtonList.get(i).setSelect(false);
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
        LayoutParams params = new LayoutParams(0, (int) getResources().getDimension(R.dimen.title_height), 1);

        //添加按钮
        ButtonBox oneLayout = new ButtonBox(getContext(), mBoxInfoList.get(0));
        oneLayout.setLayoutParams(params);
        addView(oneLayout);
        oneLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setClick(0);
            }
        });

        mButtonList.add(oneLayout);

        ButtonBox twoLayout = new ButtonBox(getContext(), mBoxInfoList.get(1));
        twoLayout.setLayoutParams(params);
        addView(twoLayout);
        twoLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setClick(1);
            }
        });
        mButtonList.add(twoLayout);

        //添加写故事按钮
        addCreateButton();

        //
        ButtonBox threeLayout = new ButtonBox(getContext(), mBoxInfoList.get(2));
        threeLayout.setLayoutParams(params);
        addView(threeLayout);
        threeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setClick(2);
            }
        });
        mButtonList.add(threeLayout);


        ButtonBox fourLayout = new ButtonBox(getContext(), mBoxInfoList.get(3));
        fourLayout.setLayoutParams(params);
        addView(fourLayout);
        fourLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setClick(3);
            }
        });
        mButtonList.add(fourLayout);

    }

    /**
     * 设置未读的消息数量
     *
     * @param position  按钮的位置
     * @param unreadNum 未读数量
     */
    public void setUnreadNum(int position, int unreadNum) {
        if (position <= mButtonList.size()) {
            mButtonList.get(position).setUnreadNum(unreadNum);
        }

    }

}
