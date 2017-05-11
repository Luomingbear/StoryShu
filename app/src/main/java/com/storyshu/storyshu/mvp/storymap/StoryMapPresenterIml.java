package com.storyshu.storyshu.mvp.storymap;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.storyshu.storyshu.adapter.card.CardAdapter;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.model.stories.StoryModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.story.StoriesAdapterView;

import java.util.ArrayList;

/**
 * mvp模式
 * 故事地图逻辑的实现
 * Created by bear on 2017/3/26.
 */

public class StoryMapPresenterIml extends IBasePresenter<StoryMapView> implements StoryMapPresenter {
    private static final String TAG = "StoryMapPresenterIml";
    private StoryModel mStoryModel; //故事的数据管理
    private ArrayList<StoryInfo> mStoryList; //卡片的数据源¬
    private int lastSelectedStoryIndex = 0; //上一次选中的故事
    private int mSignDays = 1; //累计签到的天数
    private boolean isSign = false; //是否已经签到
    private CardAdapter mCardAdapter; //故事卡片的适配器
    private boolean isStoryWindowShow = false; //故事卡片的窗口是否正显示

    public StoryMapPresenterIml(Context mContext, StoryMapView mvpView) {
        super(mContext, mvpView);
        mStoryModel = new StoryModel(mContext);
        mStoryList = new ArrayList<>();

    }

    /**
     * 故事卡片的点击事件
     */
    StoriesAdapterView.OnCardClickListener cardClickListener = new StoriesAdapterView.OnCardClickListener() {
        @Override
        public void OnCardClick(int cardIndex) {
            mMvpView.intent2StoryRoomActivity(mStoryList.get(cardIndex));
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
            mMvpView.getStoryWindow().requestLayout();

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
        Log.i(TAG, "initMap: 初始化地图");
        //初始化地图管家
        ILocationManager.getInstance().init(mContext, mMvpView.getMapView());

        /**
         * 点击和滑动地图就让故事卡片隐藏
         */
        mMvpView.getMapView().getMap().setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        //隐藏卡片
                        mMvpView.hideCardWindow();
                        //恢复显示上一次选中的
                        ILocationManager.getInstance().showDefIcon(mStoryList.get(lastSelectedStoryIndex));
                        //隐藏选中的图标
                        ILocationManager.getInstance().hideSelectedIcon();
                        isStoryWindowShow = false;
                        break;
                }
            }
        });

        //marker的点击事件
        ILocationManager.getInstance().setOnLocationMarkerClickListener(onMarkClickListener);


        //卡片显示
        mCardAdapter = new CardAdapter(mContext, mStoryList);
        mMvpView.getStoryWindow().setAdapter(mCardAdapter);
        /**
         * 故事卡片的点击,滑动监听
         */
        mMvpView.getStoryWindow().setOnCardClickListener(cardClickListener);
        mMvpView.getStoryWindow().setOnCardSlidingListener(cardSlidingListener);
    }

    /**
     * 更新故事图标
     */
    private void updateStoryIcon(StoryInfo storyInfo) {
        //恢复上一次的小图标
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
                //marker的title 是userID,snipper是故事id
                if (storyInfo.getStoryId().equals(marker.getSnippet())) {
                    updateStoryIcon(storyInfo);
                    centerIndex = i;
                    break;
                }
            }
            //显示卡片窗口
            mMvpView.getStoryWindow().setSelection(centerIndex);
            mMvpView.showCardWindow();
            isStoryWindowShow = true;

            //更新下标
            lastSelectedStoryIndex = centerIndex;
        }
    };

    /**
     * 获取位置信息
     */
    public void getLocation() {
        Log.i(TAG, "getLocation: 获取定位");
        if (!ILocationManager.getInstance().isInit())
            initMap();

        ILocationManager.getInstance().start();
    }

    /**
     * 故事数据获取的回调函数
     */
    private StoryModel.OnStoryGetListener onStoryModelListener = new StoryModel.OnStoryGetListener() {
        @Override
        public void onStoriesGot(ArrayList<StoryInfo> storyList) {
            mStoryList.clear();
            for (StoryInfo storyInfo : storyList) {
                mStoryList.add(storyInfo);
                Log.i(TAG, "onStoriesGot: path：" + storyInfo.getCover());
            }

            mCardAdapter.notifyDataSetChanged();
            //地图的故事集图标显示
            ILocationManager.getInstance().showStoriesIcons(mStoryList);
        }
    };

    /**
     * 显示故事集图标
     */
    @Override
    public void showStoryIcons() {

        mStoryModel.getNearStories(ISharePreference.getUserId(mContext),
                ISharePreference.getLatLngData(mContext), onStoryModelListener);
    }

    @Override
    public void showSignDialog() {
        if (!isSign) {
            mMvpView.showSignDialog(mSignDays);
            isSign = true;
        } else {
//            mMvpView.showToast(R.string.sign_in_yes);
        }
    }

    /**
     * 移动地图到当前的位置
     */
    public void move2Position() {
        ILocationManager.getInstance().animate2CurrentPosition();
    }
}
