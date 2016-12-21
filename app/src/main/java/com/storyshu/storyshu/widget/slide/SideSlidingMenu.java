package com.storyshu.storyshu.widget.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 侧滑菜单
 * Created by bear on 2016/12/6.
 */

public class SideSlidingMenu extends RelativeLayout {
    private View mHomePage; //主页
    private View mMenu; //菜单
    private LayoutInflater mInflater;

    public SideSlidingMenu(Context context) {
        this(context, null);
    }

    public SideSlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideSlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        showHomePage(l, t, r, b);

//        showMenu(l, t, r, b);
    }

    /**
     * 显示主页面
     */
    private void showHomePage(int l, int t, int r, int b) {
//        mHomePage = mInflater.inflate(R.layout.menu_layout, this);
//
//        mHomePage.layout(l, t, r, b);
    }

    /**
     * 显示菜单
     */
    private void showMenu(int l, int t, int r, int b) {
        mMenu = getChildAt(1);
        mMenu.layout(-r, t, l, b);
    }

    /**
     * 动画显示主页
     */
    public void animateHomePage() {

    }

    /**
     * 动画显示菜单
     */
    public void animateMenu() {

    }
}
