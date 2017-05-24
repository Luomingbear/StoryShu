package com.storyshu.storyshu.fragement;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.story.StoryRoomActivity;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.mvp.storymap.StoryMapPresenterIml;
import com.storyshu.storyshu.mvp.storymap.StoryMapView;
import com.storyshu.storyshu.tool.observable.EventObservable;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.story.StoriesAdapterView;
import com.storyshu.storyshu.widget.title.TitleView;

/**
 * 故事地图的fragment
 * Created by bear on 2017/3/11.
 */

public class StoryMapFragment extends IBaseStatusFragment implements StoryMapView {
    private static final String TAG = "StoryMapFragment";
    private static StoryMapFragment instance;
    private TextureMapView mMapView; //地图控件
    private TitleView mTitleView; //标题栏
    private StoriesAdapterView mStoryCardWindow; //故事卡片的窗口

    private int mAnimateTime = 260; //动画执行时间 毫秒
    private boolean isStoryShow = false; //故事是否隐藏了 否
    private boolean isStoryAnimate = false; //故事是否处于动画状态 否

    private StoryMapPresenterIml mStoryMapPresenter; //故事地图的逻辑实现

    public static StoryMapFragment getInstance() {
        if (instance == null) {
            synchronized (StoryMapFragment.class) {
                if (instance == null)
                    instance = new StoryMapFragment();
            }
        }
        return instance;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.story_map_layout;
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();

        ILocationManager.getInstance().destroy();
        mStoryMapPresenter.distach();

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
//
        mStoryMapPresenter.getNearStory();
    }

    @Override
    public void onPause() {
        super.onPause();
        ILocationManager.getInstance().pause();
        mMapView.onPause();

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (mRootView == null)
            return;
        //状态栏
        setStatusBackgroundColor(R.color.colorRed);
        //
        mTitleView = (TitleView) mRootView.findViewById(R.id.title_view);
        EventObservable.getInstance().addObserver(mTitleView);

        //地图
        mMapView = (TextureMapView) mRootView.findViewById(R.id.story_map_map_view);
        mMapView.onCreate(savedInstanceState);

        //故事卡片窗口
        mStoryCardWindow = (StoriesAdapterView) mRootView.findViewById(R.id.story_card_window);

        mStoryMapPresenter = new StoryMapPresenterIml(getContext(), this);
    }

    @Override
    public AMap getAMap() {
        return mMapView.getMap();
    }

    @Override
    public StoriesAdapterView getStoryWindow() {
        return mStoryCardWindow;
    }


    @Override
    public void updateStoryIcons() {
        //发布故事之后的回调，更新地图信息
        hideCardWindow();
        mStoryMapPresenter.getNearStory();
    }

    @Override
    public void showCardWindow() {
        animateStoryShow(true);
    }

    @Override
    public void hideCardWindow() {
        animateStoryShow(false);
    }

    @Override
    public void intent2StoryRoomActivity(CardInfo cardInfo) {
        intentWithParcelable(StoryRoomActivity.class, NameUtil.CARD_INFO, cardInfo);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initEvents() {
        mStoryMapPresenter.initMap();
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onCenterClick() {
                mStoryMapPresenter.move2Position();

            }

            @Override
            public void onCenterDoubleClick() {

            }

            @Override
            public void onRightClick() {

            }
        });

        mStoryMapPresenter.getLocation();
    }

    @Override
    public void showToast(String s) {
        ToastUtil.Show(getContext(), s);
    }

    @Override
    public void showToast(int stringRes) {
        ToastUtil.Show(getContext(), stringRes);
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

        /**
         * 标题栏
         */
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
}