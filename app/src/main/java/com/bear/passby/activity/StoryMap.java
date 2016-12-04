package com.bear.passby.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.bear.passby.R;
import com.bear.passby.model.location.ILocationManager;
import com.bear.passby.widget.sift.SiftWindow;
import com.bear.passby.widget.story.StoriesDialog;
import com.bear.passby.widget.title.TitleView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class StoryMap extends FragmentActivity implements View.OnClickListener, ILocationManager.OnLocationMarkerClickListener {
    private static final String TAG = "StoryMap";

    private TitleView mTitleView; //标题栏

    private MapView mMapView; //地图
    private View mCreateStory; //写故事按钮
    private long getPositionTime; //获取定位的时间
    private int minLocationIntervalTime = 10 * 1000;// 最小的获取定位的间隔时间，单位毫秒


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initImageLoader();

        setContentView(R.layout.activity_story_map);

        initView(savedInstanceState);
    }

    /**
     * 初始化图像加载器
     */
    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).build();

        ImageLoader.getInstance().init(configuration);
    }

    private void initView(Bundle savedInstanceState) {

        /**
         * 地图
         */

        mMapView = (MapView) findViewById(R.id.story_map_map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        ILocationManager.getInstance().init(getApplicationContext(), mMapView);
        getPosition();

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
         * 获取当前的定位并且移动地图
         */
        View getPosition = findViewById(R.id.story_map_get_position);
        getPosition.setOnClickListener(this);
    }

    /**
     * 标题栏点击响应事件
     */
    private TitleView.onTitleClickListener onTitleClickListener = new TitleView.onTitleClickListener() {
        @Override
        public void onLeftClick() {
            Log.i(TAG, "onLeftClick: ");

        }

        @Override
        public void onCenterClick() {
            Log.i(TAG, "onCenterClick: ");
        }

        @Override
        public void onRightClick() {
            Log.i(TAG, "onRightClick: ");
            SiftWindow siftWindow = new SiftWindow(StoryMap.this);
            siftWindow.showAsDropDown(mTitleView.getmRightButton(), 0, 0);
        }
    };

    /**
     * 标题栏
     */
    private void initTitle() {
        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setOnTitleClickListener(onTitleClickListener);
    }

    /**
     * 获取位置信息
     */
    private void getPosition() {
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
            //点击的不是个人图标
            StoriesDialog dialog = new StoriesDialog(StoryMap.this);
            dialog.setContentView(R.layout.stories_dialog_layout);
            dialog.show();
        }
    }
}
