package com.storyshu.storyshu.fragement;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.amap.api.maps.TextureMapView;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.story.StoryRoomActivity;
import com.storyshu.storyshu.bean.getStory.StoryBean;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.mvp.storymap.StoryMapPresenterIml;
import com.storyshu.storyshu.mvp.storymap.StoryMapView;
import com.storyshu.storyshu.tool.observable.EventObservable;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.dialog.SignDialog;
import com.storyshu.storyshu.widget.story.StoriesAdapterView;
import com.storyshu.storyshu.widget.title.TitleView;

import java.util.ArrayList;

/**
 * 故事地图的fragment
 * Created by bear on 2017/3/11.
 */

public class StoryMapFragment extends IBaseStatusFragment implements StoryMapView {
    private static final String TAG = "StoryMapFragment";
    private static StoryMapFragment instance;
    private TextView mSinInTv; //标题栏提示签到的文本
    private TitleView mTitleView; //标题栏
    private TextureMapView mMapView; //地图
    private StoriesAdapterView mStoryCardWindow; //故事卡片的窗口
    private View mGetLocationButton;//定位按钮
    private ArrayList<CardInfo> mCardInfoList; //数据源，卡片数据列表

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
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        ILocationManager.getInstance().destroy();
        mStoryMapPresenter.distach();

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();

        //每次返回地图页面的时候就重新获取位置，并刷新图标
        mStoryMapPresenter.getLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();

        ILocationManager.getInstance().pause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
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

        //位置的显示，观察者模式
//        NotifyTextView textView = (NotifyTextView) mRootView.findViewById(R.id.location_title);
//        textView.setQuestionId(R.id.location_title);
//        EventObservable.getInstance().addObserver(textView);

        //签到
//        mSinInTv = (TextView) mRootView.findViewById(R.id.sign_in);
//        mSinInTv.setOnClickListener(this);

        //地图
        mMapView = (TextureMapView) mRootView.findViewById(R.id.story_map_map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);

        //故事卡片窗口
        mStoryCardWindow = (StoriesAdapterView) mRootView.findViewById(R.id.story_card_window);

        /**
         * 定位按钮
         */
//        mGetLocationButton = mRootView.findViewById(R.id.get_location);
//        mGetLocationButton.setOnClickListener(this);

        mStoryMapPresenter = new StoryMapPresenterIml(getContext(), this);
    }

    @Override
    public void showSignDialog(final int signDays) {
        SignDialog signDialog = new SignDialog(getContext(), R.style.TransparentDialogTheme);
        signDialog.setTextAndShow(getString(R.string.sign_in_day_dialog, signDays));
        signDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mSinInTv.setText(getString(R.string.sign_in_day, signDays));
            }
        });
    }

    @Override
    public TextureMapView getMapView() {
        return mMapView;
    }

    @Override
    public TextView getSignInTV() {
        return mSinInTv;
    }

    @Override
    public StoriesAdapterView getStoryWindow() {
        return mStoryCardWindow;
    }

    @Override
    public View getLocationBtn() {
        return mGetLocationButton;
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
    public void intent2StoryRoomActivity(StoryBean storyInfo) {
        intentWithParcelable(StoryRoomActivity.class, NameUtil.STORY_INFO, storyInfo);
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

        //显示图标
        mStoryMapPresenter.getNearStory();
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