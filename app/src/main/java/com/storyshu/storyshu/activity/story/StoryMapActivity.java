package com.storyshu.storyshu.activity.story;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.Test;
import com.storyshu.storyshu.activity.base.IPermissionActivity;
import com.storyshu.storyshu.activity.create.CreateStoryActivity;
import com.storyshu.storyshu.activity.login.LoginActivity;
import com.storyshu.storyshu.activity.my.MyStoryActivity;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.model.stories.StoriesWindowManager;
import com.storyshu.storyshu.tool.observable.EventObservable;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.SideSlipLayout;
import com.storyshu.storyshu.widget.imageview.RoundImageView;
import com.storyshu.storyshu.widget.sift.SiftWindowManager;
import com.storyshu.storyshu.widget.title.TitleView;

import java.util.Timer;
import java.util.TimerTask;


public class StoryMapActivity extends IPermissionActivity implements View.OnClickListener, ILocationManager.OnLocationMarkerClickListener, StoriesWindowManager.OnStoryCardListener {
    private static final String TAG = "StoryMapActivity";

    private SideSlipLayout mSideSlipLayout; //侧滑布局
    private View mHomeLayout; //主界面
    private View mSideLayout; //侧滑界面

    //主界面
    private TitleView mTitleView; //标题栏
    private MapView mMapView; //地图
    private View mCreateStory; //写故事按钮
    private View mGetPosition; //定位到当前位置的按钮

    //侧边栏
    private RoundImageView mAvatar; //用户头像
    private TextView mNickName; //昵称
    private ImageView nightModeView; //夜间模式的图标
    private TextView nightModeText; //夜间模式的文字

    //
    private long getPositionTime; //获取定位的时间
    private int minLocationIntervalTime = 10 * 1000;// 最小的获取定位的间隔时间，单位毫秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_layout);

        initView(savedInstanceState);

        initEvents();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();

        if (checkAndGetPermission(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION_PERMISSION))
            getLocation();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
        ILocationManager.getInstance().stop();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();

        /**
         * 先移动镜头到上次的位置，这样不会有闪屏的感觉
         */
//        move2Position();

        //获取定位、显示图标
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        ILocationManager.getInstance().destroy();
    }

    /**
     * 标题栏点击响应事件
     */
    private TitleView.OnTitleClickListener onTitleClickListener = new TitleView.OnTitleClickListener() {
        @Override
        public void onLeftClick() {
            mSideSlipLayout.autoShowSide();
        }

        @Override
        public void onCenterClick() {
            move2Position();
        }

        @Override
        public void onCenterDoubleClick() {
        }

        @Override
        public void onRightClick() {
            Log.i(TAG, "onRightClick: ");
//            intentWithFlag(StoryListActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    };

    /**
     * 标题栏
     */
    private void initTitle() {
        mTitleView.setOnTitleClickListener(onTitleClickListener);
        EventObservable.getInstance().addObserver(mTitleView);
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setAvatar();
                    initMenuBg();
                    break;
                case 2:
                    break;
            }

        }
    };

    /**
     * 管理逻辑的异步进行
     * 先显示布局的框架，然后是地图，接着是地图的图标
     */
    private void initEvents() {
        //新建线程初始化图片加载器
        Thread imageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initImageLoader();
            }
        });
        imageThread.start();

        //头像的加载，需要获取本地的缓冲所以新建了进程
        Thread avatarThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                mhandler.sendMessage(message);
            }
        });
        avatarThread.start();

        //初始化地图管家
        ILocationManager.getInstance().init(getApplicationContext(), mMapView);
    }

    /**
     * 如果imageLoader没有初始化则初始化
     */
    private void initImageLoader() {
        if (!ImageLoader.getInstance().isInited()) {
            DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(fadeInBitmapDisplayer)
                    .showImageOnLoading(R.drawable.gray_bg).cacheInMemory(true)
                    .cacheOnDisk(true).build();
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                    .defaultDisplayImageOptions(options).build();

            ImageLoader.getInstance().init(configuration);
        }
    }

    /**
     * 设置头像
     */
    private void setAvatar() {
        String avatar = ISharePreference.getUserData(this).getAvatar();
        Log.i(TAG, "setAvatar: " + avatar);
        if (TextUtils.isEmpty(avatar))
            mAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.avatar_superman));
        else if (avatar.contains("/storage/"))
            ImageLoader.getInstance().displayImage("file://" + avatar, mAvatar);
        else
            ImageLoader.getInstance().displayImage(avatar, mAvatar);
    }

    private void initView(Bundle savedInstanceState) {
        /**
         * 整体布局
         */
        mSideSlipLayout = (SideSlipLayout) findViewById(R.id.sideSlipLayout);

        mHomeLayout = mSideSlipLayout.getHomeLayout();
        mSideLayout = mSideSlipLayout.getSideLayout();

        /**
         * 标题栏
         */
        mTitleView = (TitleView) mHomeLayout.findViewById(R.id.title_view);
        initTitle();

        /**
         * 主界面
         */
        //地图
        mMapView = (MapView) mHomeLayout.findViewById(R.id.story_map_map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);

        //写故事按钮
        mCreateStory = mHomeLayout.findViewById(R.id.story_map_create_story);
        mCreateStory.setOnClickListener(this);

        //获取当前的定位并且移动地图
//        mGetPosition = mHomeLayout.findViewById(R.id.story_map_get_position);
//        mGetPosition.setOnClickListener(this);

        /**
         * 侧边栏
         */
        mAvatar = (RoundImageView) mSideSlipLayout.getSideLayout().findViewById(R.id.menu_avatar);
        mAvatar.setOnClickListener(this);

        //
        mNickName = (TextView) mSideSlipLayout.getSideLayout().findViewById(R.id.menu_nickname);
        mNickName.setText(ISharePreference.getUserData(this).getNickname());
        mNickName.setOnClickListener(this);

        //
        View myStories = mSideSlipLayout.getSideLayout().findViewById(R.id.menu_my_stories);
        myStories.setOnClickListener(this);

        //
        View inbox = mSideSlipLayout.getSideLayout().findViewById(R.id.menu_inbox);
        inbox.setOnClickListener(this);

        //
        View collection = mSideSlipLayout.getSideLayout().findViewById(R.id.menu_collection);
        collection.setOnClickListener(this);

        //
        View setting = mSideSlipLayout.getSideLayout().findViewById(R.id.menu_setting);
        setting.setOnClickListener(this);

        //
        View nightMode = mSideSlipLayout.getSideLayout().findViewById(R.id.menu_night_mode);
        nightMode.setOnClickListener(this);

        nightModeView = (ImageView) mSideSlipLayout.getSideLayout().findViewById(R.id.menu_night_mode_view);
        nightModeText = (TextView) mSideSlipLayout.getSideLayout().findViewById(R.id.menu_night_mode_text);
    }

    /**
     * 初始化菜单栏的背景
     * 使用用户的头像进行高斯模糊
     */
    private void initMenuBg() {
        mSideSlipLayout.setLocalBlurBitmap(ISharePreference.getUserData(this).getAvatar());
    }

    /**
     * 获取位置信息
     */
    private void getLocation() {
        //显示地图上的图标，延时执行，避免卡顿
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ILocationManager.getInstance().setOnLocationMarkerClickListener(StoryMapActivity.this).start();
            }
        }, 400);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //写故事
            case R.id.story_map_create_story:
                intentTo(CreateStoryActivity.class);
//                Toast.makeText(this, R.string.activity_create_story_layout, Toast.LENGTH_SHORT).show();
                break;

            //移动到当前位置
            case R.id.story_map_get_position:
                move2Position();
                break;

            //菜单的头像
            case R.id.menu_avatar:
                intentTo(LoginActivity.class);
                break;

            //菜单的用户名
            case R.id.menu_nickname:
                break;

            //菜单的我的故事
            case R.id.menu_my_stories:
                intentTo(MyStoryActivity.class);
                break;

            //菜单的我的收件箱
            case R.id.menu_inbox:
                break;

            //菜单的我的收藏
            case R.id.menu_collection:
                break;

            //菜单的设置
            case R.id.menu_setting:
                intentTo(Test.class);
                break;

            //菜单的夜间模式
            case R.id.menu_night_mode:
                changDayOrNight();
                break;
        }
    }

    /**
     * 移动地图到当前的位置
     */
    private void move2Position() {
        ILocationManager.getInstance().move2CurrentPosition();
    }

    /**
     * 标记点点击事件
     *
     * @param marker
     */
    @Override
    public void OnMarkerClick(Marker marker) {
        Log.e(TAG, "OnMarkerClick: id:" + marker.getId());
        if (marker.getId().equals("Marker1")) {
            //点击的个人图标
        } else {
        }

        showStoryWindow();
    }

    /**
     * 显示故事集弹窗
     */
    private void showStoryWindow() {
        mCreateStory.setVisibility(View.GONE);
//        mGetPosition.setVisibility(View.GONE);

        StoriesWindowManager.getInstance().showDialog(this, this.getWindow(), mHomeLayout)
                .setOnStoryWindowListener(new StoriesWindowManager.OnStoryWindowListener() {
                    @Override
                    public void onDismiss() {
                        mCreateStory.setVisibility(View.VISIBLE);
//                        mGetPosition.setVisibility(View.VISIBLE);
                    }
                });

        /**
         * 设置点击响应的接口
         */
        StoriesWindowManager.getInstance().setOnStoryCardListener(this);
    }

    /**
     * @param position      卡片的下标
     * @param clickCardInfo
     */
    @Override
    public void onCardClick(int position, CardInfo clickCardInfo) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(StoryMapActivity.this,
                        StoriesWindowManager.getInstance().getStoryView().findViewById(R.id.card_view_detail_pic), "cover_pic");

        intentWithParcelable(StoryDetailActivity.class, options, "story", clickCardInfo);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out); //渐隐显示
    }

    /**
     * 返回按钮被点击的时候
     */
    @Override
    public void onBackPressed() {
        /**
         * 优先关闭弹出的窗口
         */
        if (StoriesWindowManager.getInstance().isShowing()) {
            StoriesWindowManager.getInstance().dismissDialog();
        } else if (mSideSlipLayout.isShowingSide()) {
            mSideSlipLayout.autoHideSide();
        } else if (SiftWindowManager.getInstance().isShowing())
            SiftWindowManager.getInstance().dismissSift();
        else
            super.onBackPressed();
    }
}