package com.storyshu.storyshu.fragement;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.story.StoryRoomActivity;
import com.storyshu.storyshu.adapter.card.CardAdapter;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.info.UserInfo;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.story.StoriesAdapterView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 故事地图的fragment
 * Created by bear on 2017/3/11.
 */

public class MapFragment extends Fragment implements ILocationManager.OnLocationMarkerClickListener {
    private static final String TAG = "MapFragment";
    private View mViewRoot; //总布局
    private MapView mMapView; //地图
    private StoriesAdapterView mStoryCardWindow; //故事卡片的窗口
    private ArrayList<CardInfo> mCardInfoList; //数据源，卡片数据列表

    private int mAnimateTime = 260; //动画执行时间 毫秒
    private boolean isStoryShow = false; //故事是否隐藏了 否
    private boolean isStoryAnimate = false; //故事是否处于动画状态 否

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.story_map_layout, container, false);
        initView(savedInstanceState);

        return mViewRoot;
    }

    /**
     * 故事卡片 隐藏、显示
     *
     * @param isShow true 显示 false 隐藏
     */
    private void animateStoryShow(boolean isShow) {
        if (isStoryAnimate || isStoryShow == isShow)
            return;

        //显示控件
        if (mStoryCardWindow.getVisibility() != View.VISIBLE)
            mStoryCardWindow.setVisibility(View.VISIBLE);

        //计算控件的高度
        float height = mStoryCardWindow.getMeasuredHeight() + getResources().getDimension(R.dimen.margin_big);

//        /**
//         * 标题栏
//         */
        height = isShow ? height : -height;

        DecelerateInterpolator dl = new DecelerateInterpolator();  //减速

        /**
         * 底部栏
         */
        float fromY = isShow ? height : 0;
        ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(mStoryCardWindow, "translationY",
                fromY, fromY - height);

        bottomAnimator.setInterpolator(dl);
        bottomAnimator.setDuration(mAnimateTime);


        bottomAnimator.start();
        bottomAnimator.addListener(animatorListener);
        isStoryShow = isShow;
    }

    /**
     * 动画监听器
     */
    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isStoryAnimate = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isStoryAnimate = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isStoryAnimate = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            isStoryAnimate = true;
        }
    };

    private void initView(Bundle savedInstanceState) {
        if (mViewRoot == null)
            return;

        //地图
        mMapView = (MapView) mViewRoot.findViewById(R.id.story_map_map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);


        //初始化地图管家
        ILocationManager.getInstance().init(getContext(), mMapView);

        //故事卡片窗口
        mStoryCardWindow = (StoriesAdapterView) mViewRoot.findViewById(R.id.story_card_window);
        addData(getContext());

        /**
         * 点击和滑动地图就让故事卡片隐藏
         */
        mMapView.getMap().setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        animateStoryShow(false);
                        break;
                }
            }
        });

        /**
         * 定位按钮
         */
        mViewRoot.findViewById(R.id.get_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 获取位置信息
     */
    public void getLocation() {
        //显示地图上的图标，延时执行，避免卡顿
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ILocationManager.getInstance().setOnLocationMarkerClickListener(MapFragment.this).start();
            }
        }, 500);
    }


    /**
     * 卡片的滑动监听
     */
    private StoriesAdapterView.OnCardSlidingListener cardSlidingListener = new StoriesAdapterView.OnCardSlidingListener() {
        @Override
        public void onAfterSlideCenterIndex(int position) {
            mStoryCardWindow.requestLayout();
        }

        @Override
        public void onAfterSlideLeftIndex(int leftIndex) {

        }
    };

    /**
     * 故事卡片的点击事件
     */
    StoriesAdapterView.OnCardClickListener cardClickListener = new StoriesAdapterView.OnCardClickListener() {
        @Override
        public void OnCardClick(int cardIndex) {
            ToastUtil.Show(getContext(), "Click " + cardIndex);
            Intent intent = new Intent();
            intent.setClass(getContext(), StoryRoomActivity.class);
            startActivity(intent);
        }

        @Override
        public void onDismissClick() {

        }
    };

    /**
     * 添加测试数据
     */
    private void addData(Context context) {
        if (mStoryCardWindow == null)
            return;
        mCardInfoList = new ArrayList<>();
        int i;
        for (i = 0; i < 6; i++) {
            CardInfo cardInfo = new CardInfo();
            cardInfo.setTitle("路人三千");
            cardInfo.setDetailPic("http://img.hb.aicdn.com/61588dbae333304cfe8510ac5183a33d30c922bf2ad93-kn7LXO_fw658");
            cardInfo.setExtract("最初不过你好，只是这世间所有斧砍刀削的相遇都不过起源于你好。");

//            cardInfo.setCreateDate(ConvertTimeUtil.convertCurrentTime(new Date()));
            UserInfo userInfo = new UserInfo();
            userInfo.setAvatar("http://img4.duitang.com/uploads/item/201512/01/20151201084252_BmJzQ.jpeg");
            userInfo.setNickname("钟无艳");
            cardInfo.setUserInfo(userInfo);
            mCardInfoList.add(cardInfo);
        }

        mCardInfoList.get(0).setDetailPic("");
        mCardInfoList.get(0).setExtract("最初不过你好");
        mCardInfoList.get(1).setDetailPic("https://img3.doubanio.com/lpic/s29059325.jpg");
        mCardInfoList.get(2).setExtract("最初不过你好，只是这世间所有斧砍刀削的相遇都不过起源于你好。最初不过你好，只是这世间所有斧砍刀削的相遇都不过起源于你好。");
        mCardInfoList.get(2).setDetailPic("");
        mCardInfoList.get(3).setDetailPic("http://img.hb.aicdn.com/03e819460466cad979f454cb001eb4e2c35f2611580ea-qkBa75_fw658");
        mCardInfoList.get(4).setDetailPic("http://img.hb.aicdn.com/df5dda0532822ab3f1317d6501ac818ee2d83c76685d6-WC54Ra_fw658");
        mCardInfoList.get(5).setDetailPic("");

        CardAdapter adapter = new CardAdapter(context, mCardInfoList);
        mStoryCardWindow.setAdapter(adapter);
        mStoryCardWindow.setOnCardSlidingListener(cardSlidingListener);
        mStoryCardWindow.setOnCardClickListener(cardClickListener);
    }

    /**
     * 显示故事集弹窗
     */
    private void showStoryWindow() {
//        mStoryCardWindow.setVisibility(View.VISIBLE);

    }

    /**
     * marker点击
     *
     * @param marker
     */
    @Override
    public void OnMarkerClick(Marker marker) {
        //描述文本 marker.getSnippet();
        animateStoryShow(true);
    }

    /**
     * 移动地图到当前的位置
     */
    private void move2Position() {
        ILocationManager.getInstance().move2CurrentPosition();
    }


}