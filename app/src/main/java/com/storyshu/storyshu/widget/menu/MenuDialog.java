package com.storyshu.storyshu.widget.menu;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.StatusBarUtil;
import com.storyshu.storyshu.widget.dialog.IBaseDialog;

/**
 * 侧滑菜单dialog
 * Created by bear on 2016/12/7.
 */

public class MenuDialog extends IBaseDialog {
    public MenuDialog(Context context) {
        this(context, 0);
    }

    public MenuDialog(Context context, int themeResId) {
        super(context, R.style.MenuDialogTheme);
    }

    public MenuDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void Create() {
        getWindow().setGravity(Gravity.LEFT); //位置
        getWindow().setWindowAnimations(R.style.menuAnimation); //设置进入退出的动画
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.dimAmount = 0.3f; //设置布局之外的空间的透明度，即背景变暗的程度，不是dialog的背景
        int statusBarHeight = StatusBarUtil.getHeight(getContext());
        params.height = display.getHeight() - statusBarHeight;
        params.width = (int) (display.getWidth() * 0.65f);
        getWindow().setAttributes(params);
    }
}
