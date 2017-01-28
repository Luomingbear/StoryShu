package com.storyshu.storyshu.activity.story;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
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


public class StoryMapActivity extends IBaseActivity implements View.OnClickListener, ILocationManager.OnLocationMarkerClickListener, StoriesWindowManager.OnStoryCardListener {
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
    private boolean isSiftShow = false; //筛选栏是否显示
    private long getPositionTime; //获取定位的时间
    private int minLocationIntervalTime = 10 * 1000;// 最小的获取定位的间隔时间，单位毫秒


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_layout);

        initView(savedInstanceState);

        initMenuBg();
//        initImageLoader();
    }

    private void initView(Bundle savedInstanceState) {

        /**
         * 整体布局
         */
        mSideSlipLayout = (SideSlipLayout) findViewById(R.id.sideSlipLayout);

        mHomeLayout = mSideSlipLayout.getHomeLayout();
        mSideLayout = mSideSlipLayout.getSideLayout();

        /**
         * 主界面
         */
        //地图
        mMapView = (MapView) mHomeLayout.findViewById(R.id.story_map_map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        ILocationManager.getInstance().init(getApplicationContext(), mMapView);

        //标题栏
        initTitle();

        //写故事
        mCreateStory = mHomeLayout.findViewById(R.id.story_map_create_story);
        mCreateStory.setOnClickListener(this);


        //获取当前的定位并且移动地图
        mGetPosition = mHomeLayout.findViewById(R.id.story_map_get_position);
        mGetPosition.setOnClickListener(this);


        /**
         * 侧边栏
         */
        mAvatar = (RoundImageView) mSideSlipLayout.getSideLayout().findViewById(R.id.menu_avatar);
        setAvatar();
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
     * 侧边菜单点击响应
     */
    // TODO: 2017/1/26

    /**
     * 标题栏点击响应事件
     */
    private TitleView.OnTitleClickListener onTitleClickListener = new TitleView.OnTitleClickListener() {
        @Override
        public void onLeftClick() {
            Log.i(TAG, "onLeftClick: ");
            mSideSlipLayout.autoShowSide();
        }

        @Override
        public void onCenterClick() {
            Log.i(TAG, "onCenterClick: ");
        }

        @Override
        public void onCenterDoubleClick() {
            move2Position();
        }

        @Override
        public void onRightClick() {
            Log.i(TAG, "onRightClick: ");
//            intentWithFlag(StoryListActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intentTo(StoryListActivity.class);
        }
    };

    /**
     * 标题栏
     */
    private void initTitle() {
        mTitleView = (TitleView) mHomeLayout.findViewById(R.id.title_view);
        mTitleView.setOnTitleClickListener(onTitleClickListener);
        EventObservable.getInstance().addObserver(mTitleView);
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

        Log.i(TAG, "initView:avatar:" + ISharePreference.getUserData(this).getAvatar());

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
//        ILocationManager.getInstance().stop();
        ILocationManager.getInstance().setOnLocationMarkerClickListener(this).start();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
        ILocationManager.getInstance().stop();

//        StoriesWindowManager.getInstance().dismissDialog();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
        getLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        ILocationManager.getInstance().init(getApplicationContext(), mMapView).destroy();
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

            //菜单的用户名
            case R.id.menu_my_stories:
                intentTo(MyStoryActivity.class);
                break;

            //菜单的用户名
            case R.id.menu_inbox:
                break;

            //菜单的用户名
            case R.id.menu_collection:
                break;

            //菜单的用户名
            case R.id.menu_setting:
                break;

            //菜单的用户名
            case R.id.menu_night_mode:
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
        mGetPosition.setVisibility(View.GONE);

        StoriesWindowManager.getInstance().showDialog(this, this.getWindow(), mHomeLayout)
                .setOnStoryWindowListener(new StoriesWindowManager.OnStoryWindowListener() {
                    @Override
                    public void onDismiss() {
                        mCreateStory.setVisibility(View.VISIBLE);
                        mGetPosition.setVisibility(View.VISIBLE);
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
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
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
