package com.storyshu.storyshu.mvp.storymap;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.card.CardAdapter;
import com.storyshu.storyshu.bean.getStory.StoryBean;
import com.storyshu.storyshu.bean.like.LikePostBean;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.model.LikeModel;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.model.stories.StoryModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.story.StoriesAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * mvp模式
 * 故事地图逻辑的实现
 * Created by bear on 2017/3/26.
 */

public class StoryMapPresenterIml extends IBasePresenter<StoryMapView> implements StoryMapPresenter {
    private static final String TAG = "StoryMapPresenterIml";
    private StoryModel mStoryModel; //故事的数据管理
    private ArrayList<CardInfo> mStoryList; //卡片的数据源¬
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

    private float downX, downY; //手指按下的坐标
    /**
     * 地图滑动响应事件
     */
    AMap.OnMapTouchListener onMapTouchListener = new AMap.OnMapTouchListener() {
        @Override
        public void onTouch(MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = motionEvent.getX();
                    downY = motionEvent.getY();
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:

                    if (Math.abs(motionEvent.getX() - downX) > 100
                            || Math.abs(motionEvent.getY() - downY) > 100) {

                        //更新数据源
                        getNearStory();
                        Log.i(TAG, "onTouch: 更新！！！");
                    } else {
                        //隐藏卡片
                        //恢复显示上一次选中的
                        ILocationManager.getInstance().showDefIcon(mStoryList.get(lastSelectedStoryIndex));
                        //隐藏选中的图标
                        ILocationManager.getInstance().hideSelectedIcon();
                    }

                    //隐藏卡片
                    mMvpView.hideCardWindow();
                    isStoryWindowShow = false;
                    break;
            }
        }
    };

    /**
     * 喜欢故事
     *
     * @param position
     */
    private void likeStory(final int position) {
        LikeModel likeModel = new LikeModel(mContext);
        LikePostBean likePostBean = new LikePostBean();
        likePostBean.setUserId(ISharePreference.getUserId(mContext));
        likePostBean.setStoryId(mStoryList.get(position).getStoryId());
        // TODO: 2017/5/15 判断默认是喜欢还是不喜欢 
        likePostBean.setSure(true);
        likeModel.likeStory(likePostBean);
        likeModel.setOnLikeListener(new LikeModel.OnLikeListener() {
            @Override
            public void onSucceed() {
                mMvpView.showToast(R.string.like);
                mStoryList.get(position).setLike(true);
                mCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String error) {
                mMvpView.showToast(error);
            }
        });
    }

    /**
     * 不喜欢故事
     *
     * @param position
     */
    private void opposeStory(int position) {

    }


    private CardAdapter.OnCardClickedListener onCardClickedListener = new CardAdapter.OnCardClickedListener() {
        @Override
        public void onLikeClick(int p) {
            likeStory(p);
        }

        @Override
        public void onOpposeClick(int p) {
            opposeStory(p);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {

            } else if (msg.what == 2) {
                getNearStory();
            }
        }
    };

    @Override
    public void initMap() {
        Log.i(TAG, "initMap: 初始化地图");
        //初始化地图管家
        ILocationManager.getInstance().init(mContext, mMvpView.getAMap());

        /**
         * 点击和滑动地图就让故事卡片隐藏
         */
        mMvpView.getAMap().setOnMapTouchListener(onMapTouchListener);

        //marker的点击事件
        ILocationManager.getInstance().setOnLocationMarkerClickListener(onMarkClickListener);

        //卡片显示
        mCardAdapter = new CardAdapter(mContext, mStoryList);
        mCardAdapter.setOnCardClickListener(onCardClickedListener);
        mMvpView.getStoryWindow().setAdapter(mCardAdapter);

        /**
         * 故事卡片的点击,滑动监听
         */
        mMvpView.getStoryWindow().setOnCardClickListener(cardClickListener);
        mMvpView.getStoryWindow().setOnCardSlidingListener(cardSlidingListener);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        }, 2 * 1000);
    }

    /**
     * 更新故事图标
     */
    private void updateStoryIcon(StoryBean storyInfo) {
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
                StoryBean storyInfo = mStoryList.get(i);
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
        public void onStoriesGot(List<StoryBean> storyList) {
            mStoryList.clear();
            for (StoryBean storyInfo : storyList) {
                mStoryList.add(new CardInfo(storyInfo));
                Log.i(TAG, "onStoriesGot: path：" + storyInfo.getCover());
            }

            mCardAdapter.notifyDataSetChanged();
            //地图的故事集图标显示
            ILocationManager.getInstance().showStoriesIcons(storyList);
        }

        @Override
        public void onFailed(String error) {
            mMvpView.showToast(error);
        }
    };

    /**
     * 显示故事集图标
     */
    @Override
    public void getNearStory() {
        Log.i(TAG, "getNearStory: 刷新故事。。。");
        mStoryModel.getNearStories(ISharePreference.getUserId(mContext),
                ISharePreference.getLatLngData(mContext), (int) mMvpView.getAMap().getCameraPosition().zoom);
        Log.i(TAG, "getNearStory: zoom:" + (int) mMvpView.getAMap().getCameraPosition().zoom);
        mStoryModel.setOnStoryModelListener(onStoryModelListener);

    }

    /**
     * 移动地图到当前的位置
     */
    public void move2Position() {
        ILocationManager.getInstance().animate2CurrentPosition();
    }
}
