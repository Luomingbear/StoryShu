package com.storyshu.storyshu.widget.menu;

import android.content.Context;

import com.storyshu.storyshu.R;

/**
 * 菜单弹窗的管家
 * Created by bear on 2016/12/7.
 */

public class MenuDialogManager {
    private static MenuDialogManager instance; //单例对象
    private MenuDialog mMenuDialog; //弹窗对象

    public static MenuDialogManager getInstance() {
        if (instance == null) {

            synchronized (MenuDialogManager.class) {
                if (instance == null)
                    instance = new MenuDialogManager();
            }
        }
        return instance;
    }

    protected MenuDialogManager() {
    }

    /**
     * 显示菜单弹窗
     *
     * @param context
     */
    public void showMenu(Context context) {
        if (mMenuDialog != null && mMenuDialog.isShowing()) {
            return;
        }

        mMenuDialog = new MenuDialog(context);
        mMenuDialog.setContentView(R.layout.menu_layout);

        mMenuDialog.show();
    }

    /**
     * 让菜单弹窗消失
     */
    public void dismissMenu() {
        if (mMenuDialog == null)
            return;
        mMenuDialog.dismiss();
    }

    /***
     * 菜单弹窗是否处于显示状态
     *
     * @return 是否处于显示状态
     */
    public boolean isShowing() {
        if (mMenuDialog == null)
            return false;
        return mMenuDialog.isShowing();
    }
}
