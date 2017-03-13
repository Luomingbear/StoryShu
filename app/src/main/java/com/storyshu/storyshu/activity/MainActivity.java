package com.storyshu.storyshu.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IPermissionActivity;
import com.storyshu.storyshu.fragement.AirportFragment;
import com.storyshu.storyshu.fragement.MapFragment;
import com.storyshu.storyshu.fragement.MessageFragment;
import com.storyshu.storyshu.fragement.MineFragment;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.CreateButton;
import com.storyshu.storyshu.widget.blurRelativeLayout.BottomNavigationBar;

public class MainActivity extends IPermissionActivity {
    private MapFragment mStoryMapFragment; //地图fragment；
    private AirportFragment mAirpotFragment; //候机厅fragment；
    private MessageFragment mMessageFragment; //消息fragment；
    private MineFragment mMeFragment; //我的fragment；
    private FragmentManager mFragmentManager; //fragment管家

    private CreateButton mCreateButton; //创建故事的按钮

    TextView showText;
    private BottomNavigationBar mNavigationBar; //底部导航栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_tab_layout);

        initView();

        mFragmentManager = getSupportFragmentManager();
        setSelection(0); //默认显示第一页
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * 查看定位权限是否打开，打开才能开始定位
         */
        if (checkAndGetPermission(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION_PERMISSION))
            mStoryMapFragment.getLocation();
    }

    /**
     * 底部导航栏的点击反馈
     */
    private BottomNavigationBar.OnBottomNavigationClickListener navigationClickListener =
            new BottomNavigationBar.OnBottomNavigationClickListener() {
                @Override
                public void onClick(int position) {
                    switch (position) {
                        case 0:
                            setSelection(0);
                            break;

                        case 1:
                            setSelection(1);
                            break;

                        case 2:
                            setSelection(2);
                            break;

                        case 3:
                            setSelection(3);
                            break;
                    }
                }
            };

    /**
     * 创建故事按钮被点击
     */
    private CreateButton.OnCreateClickListener createClickListener = new CreateButton.OnCreateClickListener() {
        @Override
        public void onClick() {
            ToastUtil.Show(MainActivity.this, R.string.create_story);
        }
    };

    /**
     * 初始化布局
     */
    private void initView() {

        //创建故事按钮
        mCreateButton = (CreateButton) findViewById(R.id.create_story);
        mCreateButton.setCreateClickListener(createClickListener);

        //底部导航栏
        mNavigationBar = (BottomNavigationBar) findViewById(R.id.navigation_bar);
        mNavigationBar.setNavigationClickListener(navigationClickListener);

    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。
     */
    private void setSelection(int index) {
        //开始
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (mStoryMapFragment == null) {
                    mStoryMapFragment = new MapFragment();
                    transaction.add(R.id.content, mStoryMapFragment);
                } else transaction.show(mStoryMapFragment);
                break;

            case 1:
                if (mAirpotFragment == null) {
                    mAirpotFragment = new AirportFragment();
                    transaction.add(R.id.content, mAirpotFragment);
                } else transaction.show(mAirpotFragment);
                break;

            case 2:
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    transaction.add(R.id.content, mMessageFragment);
                } else transaction.show(mMessageFragment);
                break;

            case 3:
                if (mMeFragment == null) {
                    mMeFragment = new MineFragment();
                    transaction.add(R.id.content, mMeFragment);
                } else transaction.show(mMeFragment);
                break;
        }

        transaction.commit(); //提交
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mStoryMapFragment != null) {
            transaction.hide(mStoryMapFragment);
        }
        if (mAirpotFragment != null) {
            transaction.hide(mAirpotFragment);
        }
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }
}