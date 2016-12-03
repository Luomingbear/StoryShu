package com.bear.passby.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.amap.api.maps2d.MapView;
import com.bear.passby.R;
import com.bear.passby.model.location.ILocationManager;


public class StoryMap extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "StoryMap";

    private MapView mMapView; //地图
    private View mCreateStory; //写故事按钮
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
        getPosition();

        /**
         * 标题栏
         */


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

        ILocationManager.getInstance().start();

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


}
