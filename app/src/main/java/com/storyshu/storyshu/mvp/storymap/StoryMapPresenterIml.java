package com.storyshu.storyshu.mvp.storymap;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.card.CardAdapter;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.model.stories.StoryModel;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.story.StoriesAdapterView;

import java.util.ArrayList;

/**
 * mvp模式
 * 故事地图逻辑的实现
 * Created by bear on 2017/3/26.
 */

public class StoryMapPresenterIml implements StoryMapPresenter {
    private static final String TAG = "StoryMapPresenterIml";
    private StoryMapView mStoryMapView;
    private Context mContext;
    private StoryModel mStoryModel; //故事的数据管理
    private ArrayList<StoryInfo> mStoryList; //卡片的数据源¬
    private int lastSelectedStoryIndex = 0; //上一次选中的故事
    private int mSignDays = 1; //累计签到的天数
    private boolean isSign = false; //是否已经签到

    public StoryMapPresenterIml(StoryMapView mStoryMapView, Context mContext) {
        this.mStoryMapView = mStoryMapView;
        this.mContext = mContext;
        mStoryModel = new StoryModel(mContext);
    }

    /**
     * 故事卡片的点击事件
     */
    StoriesAdapterView.OnCardClickListener cardClickListener = new StoriesAdapterView.OnCardClickListener() {
        @Override
        public void OnCardClick(int cardIndex) {
            mStoryMapView.intent2StoryRoomActivity(mStoryList.get(cardIndex));
        }

        @Override
        public void onDismissClick() {

        }
    };
    /**
     * 卡片的滑动监听
     */
    private StoriesAdapterView.OnCardSlidingListener cardSlidingListener = new StoriesAdapterView.OnCardSlidingListener() {
        @Override
        public void onAfterSlideCenterIndex(int position) {
            //更新卡片
            mStoryMapView.getStoryWindow().requestLayout();

            //有变化才更新
            if (lastSelectedStoryIndex != position) {
                updateStoryIcon(mStoryList.get(position));
                //更新上一次选中的下标
                lastSelectedStoryIndex = position;
            }
        }

        @Override
        public void onAfterSlideLeftIndex(int leftIndex) {

        }
    };

    @Override
    public void initMap() {
        //初始化地图管家
        ILocationManager.getInstance().init(mContext, mStoryMapView.getMapView());

        /**
         * 点击和滑动地图就让故事卡片隐藏
         */
        mStoryMapView.getMapView().getMap().setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        //隐藏卡片
                        mStoryMapView.hideCardWindow();
                        //更新上一次选中的图标,设置为普通模式
                        ILocationManager.getInstance().showDefIcon(mStoryList.get(lastSelectedStoryIndex));
                        break;
                }
            }
        });

        /**
         * 故事卡片的点击,滑动监听
         */
        mStoryMapView.getStoryWindow().setOnCardClickListener(cardClickListener);
        mStoryMapView.getStoryWindow().setOnCardSlidingListener(cardSlidingListener);
    }

    /**
     * 故事数据获取的回调函数
     */
    private StoryModel.OnStoryModelListener onStoryModelListener = new StoryModel.OnStoryModelListener() {
        @Override
        public void onStoriesGot(ArrayList<StoryInfo> storyList) {
            mStoryList = storyList;

            showStoryIcons();
        }
    };

    @Override
    public void showMap() {
        getLocation();

        mStoryModel.getNearStories(ISharePreference.getUserData(mContext)
                , ISharePreference.getLatLngPointData(mContext), onStoryModelListener);
    }

    /**
     * 更新故事图标
     */
    private void updateStoryIcon(StoryInfo storyInfo) {
        //更新上一次选中的图标
        ILocationManager.getInstance().showDefIcon(mStoryList.get(lastSelectedStoryIndex));
        //更新地图上的新选中的图标
        ILocationManager.getInstance().showSelectedIcon(storyInfo);
    }

    /**
     * marker点击
     */
    private ILocationManager.OnLocationMarkerClickListener onMarkClickListener
            = new ILocationManager.OnLocationMarkerClickListener() {
        @Override
        public void OnMarkerClick(Marker marker) {
            //设置显示新的图标
            int centerIndex = 0;
            for (int i = 0; i < mStoryList.size(); i++) {
                StoryInfo storyInfo = mStoryList.get(i);
                if (storyInfo.getLatLng().equals(marker.getPosition())) {
                    updateStoryIcon(storyInfo);
                    lastSelectedStoryIndex = centerIndex;
                    centerIndex = i;
                    break;
                }
            }
            //显示卡片窗口
            Log.i(TAG, "OnMarkerClick: marker position:" + marker.getPosition());
            Log.i(TAG, "OnMarkerClick: ");
            Log.i(TAG, "OnMarkerClick: centerIndex:" + centerIndex);
            mStoryMapView.getStoryWindow().setCenterCardIndex(centerIndex);
            mStoryMapView.showCardWindow();
        }
    };

    /**
     * 获取位置信息
     */
    public void getLocation() {
        if (!ILocationManager.getInstance().isInit())
            initMap();
        ILocationManager.getInstance().setOnLocationMarkerClickListener(onMarkClickListener).start();
    }

    /**
     * 显示故事集图标
     */
    @Override
    public void showStoryIcons() {
        CardAdapter cardAdapter = new CardAdapter(mContext, mStoryList);
        mStoryMapView.getStoryWindow().setAdapter(cardAdapter);

        //地图的故事集图标显示
        ILocationManager.getInstance().showStoriesIcons(mStoryList);
    }

    @Override
    public void showSignDialog() {
        if (!isSign) {
            mStoryMapView.showSignDialog(mSignDays);
            isSign = true;
        } else {
            mStoryMapView.showToast(R.string.sign_in_succeed);
        }
    }

    /**
     * 移动地图到当前的位置
     */
    private void move2Position() {
        ILocationManager.getInstance().animate2CurrentPosition();
    }
}
