package com.storyshu.storyshu.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.SupportMapFragment;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.PushService;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IPermissionActivity;
import com.storyshu.storyshu.activity.create.CreateArticleActivity;
import com.storyshu.storyshu.activity.create.CreateStoryActivity;
import com.storyshu.storyshu.fragement.AirportFragment;
import com.storyshu.storyshu.fragement.MessageFragment;
import com.storyshu.storyshu.fragement.MineFragment;
import com.storyshu.storyshu.fragement.StoryMapFragment;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.mvp.main.MainPresenterIml;
import com.storyshu.storyshu.mvp.main.MainView;
import com.storyshu.storyshu.tool.PushIntentService;
import com.storyshu.storyshu.tool.observable.EventObservable;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.BottomNavigationBar;
import com.storyshu.storyshu.widget.CreateButton;
import com.storyshu.storyshu.widget.LineProgressBar;

public class MainActivity extends IPermissionActivity implements MainView {

    private static final String TAG = "MainActivity";
    private StoryMapFragment mStoryMapFragment; //地图界面fragment；
    private SupportMapFragment mMapFragment; //地图控件fragment
    private AirportFragment mAirportFragment; //候机厅fragment；
    private MessageFragment mMessageFragment; //消息fragment；
    private MineFragment mMeFragment; //我的fragment；
    private FragmentManager mFragmentManager; //fragment管家

    private View mTranslateView; //半透明的view，当新建故事选项显示的时候显示
    private CreateButton mCreateButton; //创建故事的按钮

    private BottomNavigationBar mNavigationBar; //底部导航栏

    private MainPresenterIml mMainPresenterIml; //主页的逻辑实现

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_tab_layout);

        initView();

        mMainPresenterIml = new MainPresenterIml(MainActivity.this, MainActivity.this);

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
        checkAndGetPermission(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION_PERMISSION);

        //获取未读的消息数量
        mMainPresenterIml.setUnreadNum();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainPresenterIml.stopTimer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NameUtil.REQUST_CREATE) {
//            回调更新图标
            StoryMapFragment.getInstance().updateStoryIcons();
        } else if (requestCode == PERMISSION_INTENT) {
            ILocationManager.getInstance().init(getApplicationContext(), StoryMapFragment.getInstance().getAMap()).start();
        }
    }


    @Override
    public BottomNavigationBar getBottomNavigationBar() {
        return mNavigationBar;
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
        public void showingStoryType() {
            Log.i(TAG, "showingStoryType: ");
            mTranslateView.setVisibility(View.VISIBLE);
        }

        @Override
        public void dismissStoryType() {
            Log.i(TAG, "dismissStoryType: ");
            mTranslateView.setVisibility(View.GONE);
        }

        @Override
        public void onClick(int position) {
            //跳转页面
            Intent intent = new Intent();
            switch (position) {
                case 0:
                    intent.setClass(MainActivity.this, CreateArticleActivity.class);
                    startActivityForResult(intent, NameUtil.REQUST_CREATE);
                    break;
                case 1: //短文字
                    intent.setClass(MainActivity.this, CreateStoryActivity.class);
                    startActivityForResult(intent, NameUtil.REQUST_CREATE);
                    break;
                case 2:
                    break;
            }


        }
    };

    /**
     * 初始化布局
     */
    private void initView() {
        //状态栏
        StatusBarUtils.setTranslucentForImageViewInFragment(MainActivity.this, null);

        //半透明view
        mTranslateView = findViewById(R.id.translate_view);

        //创建故事按钮
        mCreateButton = (CreateButton) findViewById(R.id.create_story);
        mCreateButton.setCreateClickListener(createClickListener);

        //底部导航栏
        mNavigationBar = (BottomNavigationBar) findViewById(R.id.navigation_bar);
        mNavigationBar.setNavigationClickListener(navigationClickListener);

        //设置默认显示第一个fragment
        mFragmentManager = getSupportFragmentManager();
        setSelection(0); //默认显示第一页


        //统一的进度条
        LineProgressBar lineProgressBar = (LineProgressBar) findViewById(R.id.line_progress_bar);
        EventObservable.getInstance().addObserver(lineProgressBar); //添加到观察者
    }

    @Override
    public void checkStorgePermission() {
        if (checkAndGetPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, FILE_PERMISSION)) {
            mMainPresenterIml.downloadNewApp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FILE_PERMISSION) {
            if (checkAndGetPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, FILE_PERMISSION)) {
                mMainPresenterIml.downloadNewApp();
            }
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //获取版本信息
        mMainPresenterIml.checkForUpdate();

        mTranslateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreateButton.setShowStoryType(false);
            }
        });

        //
        // 第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
        // 自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(),
                PushIntentService.class);

        mMainPresenterIml.addConnectListener();

        mMainPresenterIml.initEMConversation();
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
                //检查位置权限
                checkAndGetPermission(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION_PERMISSION);
                //
                if (mStoryMapFragment == null) {
                    mStoryMapFragment = StoryMapFragment.getInstance();

                    transaction.add(R.id.content, mStoryMapFragment);
                } else {
                    transaction.show(mStoryMapFragment);
                }
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

    @Override
    public void showToast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.Show(MainActivity.this, s);

            }
        });
    }

    @Override
    public void showToast(final int stringRes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.Show(MainActivity.this, stringRes);
            }
        });
    }
}