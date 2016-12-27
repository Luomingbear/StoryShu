package com.storyshu.storyshu.activity.storymap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.activity.create.CreateStoryActivity;
import com.storyshu.storyshu.activity.story.StoryDetailActivity;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.model.stories.StoriesWindowManager;
import com.storyshu.storyshu.tool.observable.EventObservable;
import com.storyshu.storyshu.widget.menu.MenuDialogManager;
import com.storyshu.storyshu.widget.sift.SiftWindowManager;
import com.storyshu.storyshu.widget.title.TitleView;


public class StoryMapActivity extends IBaseActivity implements View.OnClickListener, ILocationManager.OnLocationMarkerClickListener, StoriesWindowManager.OnStoryCardListener {
    private static final String TAG = "StoryMapActivity";

    private TitleView mTitleView; //标题栏
    private MapView mMapView; //地图
    private View mCreateStory; //写故事按钮
    private View mGetPosition; //定位到当前位置的按钮
    private View mDarkView; //半透明的黑色背景
    private boolean isSiftShow = false; //筛选栏是否显示
    private long getPositionTime; //获取定位的时间
    private int minLocationIntervalTime = 10 * 1000;// 最小的获取定位的间隔时间，单位毫秒


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_story_map);

        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {

        /**
         * 地图
         */

        mMapView = (MapView) findViewById(R.id.story_map_map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        ILocationManager.getInstance().init(getApplicationContext(), mMapView);
        getLocation();

        /**
         * 标题栏
         */
        initTitle();

        /**
         * 写故事
         */
        mCreateStory = findViewById(R.id.story_map_create_story);
        mCreateStory.setOnClickListener(this);

        /**
         * 黑色的背景
         */
        mDarkView = findViewById(R.id.story_map_dark_view);

        /**
         * 获取当前的定位并且移动地图
         */
        mGetPosition = findViewById(R.id.story_map_get_position);
        mGetPosition.setOnClickListener(this);
    }

    /**
     * 标题栏点击响应事件
     */
    private TitleView.onTitleClickListener onTitleClickListener = new TitleView.onTitleClickListener() {
        @Override
        public void onLeftClick() {
            Log.i(TAG, "onLeftClick: ");
//            StoriesWindowManager.getInstance().showDialog(StoryMapActivity.this);
            MenuDialogManager.getInstance().showMenu(StoryMapActivity.this);
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
            SiftWindowManager.getInstance().showSift(StoryMapActivity.this, mTitleView.getRightButton());
        }
    };

    /**
     * 标题栏
     */
    private void initTitle() {
        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setOnTitleClickListener(onTitleClickListener);
        EventObservable.getInstance().addObserver(mTitleView);
    }

    /**
     * 获取位置信息
     */
    private void getLocation() {
//        long time = System.currentTimeMillis();
//        //如果当前的时间与上一次获取定位的时间差不到10s，则不重复进行定位
//        if (time - getPositionTime < minLocationIntervalTime) {
//            ILocationManager.getInstance().move2CurrentPosition();
//            return;
//        }
//        getPositionTime = time;
        ILocationManager.getInstance().stop();

        ILocationManager.getInstance().setOnLocationMarkerClickListener(this).start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        move2Position();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        ILocationManager.getInstance().init(getApplicationContext(), mMapView).stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
//        StoriesWindowManager.getInstance().dismissDialog();
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
            case R.id.story_map_create_story:
                intentTo(CreateStoryActivity.class);
//                Toast.makeText(this, R.string.create_story, Toast.LENGTH_SHORT).show();
                break;

            case R.id.story_map_get_position:
                move2Position();
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
    public void OnClick(Marker marker) {
        Log.e(TAG, "OnClick: id:" + marker.getId());
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
        mDarkView.setVisibility(View.VISIBLE);
        mCreateStory.setVisibility(View.GONE);
        mGetPosition.setVisibility(View.GONE);
        StoriesWindowManager.getInstance().showDialog(this, getWindow(), mMapView).setOnStoryWindowListener(new StoriesWindowManager.OnStoryWindowListener() {
            @Override
            public void onDismiss() {
                mDarkView.setVisibility(View.GONE);
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
        intentWithParcelable(StoryDetailActivity.class, "story", clickCardInfo);
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
        } else if (MenuDialogManager.getInstance().isShowing()) {
            MenuDialogManager.getInstance().dismissMenu();
        } else if (SiftWindowManager.getInstance().isShowing())
            SiftWindowManager.getInstance().dismissSift();
        else
            super.onBackPressed();
    }
}
