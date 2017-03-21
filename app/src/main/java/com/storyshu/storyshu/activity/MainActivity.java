package com.storyshu.storyshu.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IPermissionActivity;
import com.storyshu.storyshu.activity.story.CreateStoryActivity;
import com.storyshu.storyshu.fragement.AirportFragment;
import com.storyshu.storyshu.fragement.MapFragment;
import com.storyshu.storyshu.fragement.MessageFragment;
import com.storyshu.storyshu.fragement.MineFragment;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.widget.CreateButton;
import com.storyshu.storyshu.widget.blurRelativeLayout.BottomNavigationBar;

public class MainActivity extends IPermissionActivity {
    private MapFragment mStoryMapFragment; //地图fragment；
    private AirportFragment mAirportFragment; //候机厅fragment；
    private MessageFragment mMessageFragment; //消息fragment；
    private MineFragment mMeFragment; //我的fragment；
    private FragmentManager mFragmentManager; //fragment管家

    private CreateButton mCreateButton; //创建故事的按钮

    private BottomNavigationBar mNavigationBar; //底部导航栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_tab_layout);

        initView();

        initEvent();

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
            intentTo(CreateStoryActivity.class);
        }
    };

    /**
     * 初始化布局
     */
    private void initView() {
        //状态栏
        StatusBarUtils.setTranslucentForImageViewInFragment(MainActivity.this, null);
//        mFakeStatusBar = findViewById(R.id.fake_statusbar_view);
//        mFakeStatusBar.setMinimumHeight(StatusBarUtils.getStatusBarHeight(MainActivity.this));

        //创建故事按钮
        mCreateButton = (CreateButton) findViewById(R.id.create_story);
        mCreateButton.setCreateClickListener(createClickListener);

        //底部导航栏
        mNavigationBar = (BottomNavigationBar) findViewById(R.id.navigation_bar);
        mNavigationBar.setNavigationClickListener(navigationClickListener);

        //设置默认显示第一个fragment
        mFragmentManager = getSupportFragmentManager();
        setSelection(0); //默认显示第一页

    }

    /**
     * 如果imageLoader没有初始化则初始化
     */
    private void initImageLoader() {
        //新建线程初始化图片加载器
        Thread imageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!ImageLoader.getInstance().isInited()) {
//                    FadeInBitmapDisplayer fadeInBitmapDisplayer = new FadeInBitmapDisplayer(200, true, false, false);
                    DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
//                            .displayer(fadeInBitmapDisplayer)
                            .showImageOnLoading(R.drawable.gray_bg).cacheInMemory(true)
                            .cacheOnDisk(true).build();

                    ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(MainActivity.this)
                            .defaultDisplayImageOptions(options).build();

                    ImageLoader.getInstance().init(configuration);
                }
            }
        });
        imageThread.start();

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //初始化图片加载器
        initImageLoader();

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
                if (mAirportFragment == null) {
                    mAirportFragment = new AirportFragment();
                    transaction.add(R.id.content, mAirportFragment);
                } else transaction.show(mAirportFragment);
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
        if (mAirportFragment != null) {
            transaction.hide(mAirportFragment);
        }
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }
}