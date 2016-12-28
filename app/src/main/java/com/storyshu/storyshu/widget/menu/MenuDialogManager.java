package com.storyshu.storyshu.widget.menu;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.imageview.RoundImageView;

/**
 * 菜单弹窗的管家
 * Created by bear on 2016/12/7.
 */

public class MenuDialogManager implements View.OnClickListener {
    private static MenuDialogManager instance; //单例对象
    private MenuDialog mMenuDialog; //弹窗对象
    private RoundImageView mAvatar; //用户头像
    private TextView mNickName; //昵称
    private Context mContext;
    private ImageView nightModeView; //夜间模式的图标
    private TextView nightModeText; //夜间模式的文字
    private boolean isNight = false;

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
        mAvatar.setOnClickListener(this);

        //
        mNickName = (TextView) mMenuDialog.findViewById(R.id.menu_nickname);
        mNickName.setText(ISharePreference.getUserData(mContext).getNickname());
        mNickName.setOnClickListener(this);

        //
        View myStories = mMenuDialog.findViewById(R.id.menu_my_stories);
        myStories.setOnClickListener(this);

        //
        View inbox = mMenuDialog.findViewById(R.id.menu_inbox);
        inbox.setOnClickListener(this);

        //
        View collection = mMenuDialog.findViewById(R.id.menu_collection);
        collection.setOnClickListener(this);

        //
        View setting = mMenuDialog.findViewById(R.id.menu_setting);
        setting.setOnClickListener(this);

        //
        View nightMode = mMenuDialog.findViewById(R.id.menu_night_mode);
        nightMode.setOnClickListener(this);

        nightModeView = (ImageView) mMenuDialog.findViewById(R.id.menu_night_mode_view);
        nightModeText = (TextView) mMenuDialog.findViewById(R.id.menu_night_mode_text);
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

    private OnMenuClickListener onMenuClickListener;

    public MenuDialogManager setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_avatar:
                if (onMenuClickListener != null)
                    onMenuClickListener.OnAvatarClick();
                break;
            case R.id.menu_my_stories:
                if (onMenuClickListener != null)
                    onMenuClickListener.OnMyStoriesClick();
                break;
            case R.id.menu_inbox:
                if (onMenuClickListener != null)
                    onMenuClickListener.OnInBoxClick();
                break;
            case R.id.menu_collection:
                if (onMenuClickListener != null)
                    onMenuClickListener.OnCollectionClick();
                break;
            case R.id.menu_setting:
                if (onMenuClickListener != null)
                    onMenuClickListener.OnSettingClick();
                break;
            case R.id.menu_night_mode:
                changeNightModeShow();
                if (onMenuClickListener != null)
                    onMenuClickListener.OnNightModeClick();
                break;
        }
    }

    private void changeNightModeShow() {
        if (isNight) {
            nightModeText.setText(R.string.sun_mode);
            nightModeView.setBackgroundResource(R.drawable.sun);
        } else {
            nightModeText.setText(R.string.night_mode);
            nightModeView.setBackgroundResource(R.drawable.night);
        }
        isNight = !isNight;
    }

    public interface OnMenuClickListener {
        /**
         * 点击头像
         */
        void OnAvatarClick();

        /**
         * 我的故事
         */
        void OnMyStoriesClick();

        /**
         * 我的信件
         */
        void OnInBoxClick();

        /**
         * 我的收藏
         */
        void OnCollectionClick();

        /**
         * 设置
         */
        void OnSettingClick();

        /**
         * 夜间模式
         */
        void OnNightModeClick();
    }
}
