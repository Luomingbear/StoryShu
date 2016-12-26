package com.storyshu.storyshu.widget.menu;

import android.content.Context;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.imageview.RoundImageView;

/**
 * 菜单弹窗的管家
 * Created by bear on 2016/12/7.
 */

public class MenuDialogManager {
    private static MenuDialogManager instance; //单例对象
    private MenuDialog mMenuDialog; //弹窗对象
    private RoundImageView mAvatar; //用户头像
    private TextView mNickName; //昵称
    private Context mContext;

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

        mContext = context;
        mMenuDialog = new MenuDialog(context);
        mMenuDialog.setContentView(R.layout.menu_layout);
        mMenuDialog.show();
        initView();
    }

    private void initView() {
        mAvatar = (RoundImageView) mMenuDialog.findViewById(R.id.menu_avatar);
        ImageLoader.getInstance().displayImage(ISharePreference.getUserData(mContext).getAvatar(), mAvatar);

        //
        mNickName = (TextView) mMenuDialog.findViewById(R.id.menu_nickname);
        mNickName.setText(ISharePreference.getUserData(mContext).getNickname());
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
