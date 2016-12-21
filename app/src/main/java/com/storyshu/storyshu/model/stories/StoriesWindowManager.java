package com.storyshu.storyshu.model.stories;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.CardAdapter;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.info.UserInfo;
import com.storyshu.storyshu.widget.story.StoriesAdapterView;
import com.storyshu.storyshu.widget.story.StoriesWindow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 故事集管家
 * Created by bear on 2016/12/4.
 */

public class StoriesWindowManager implements StoriesAdapterView.OnCardSlidingListener, StoriesAdapterView.OnCardClickListener {
    private static StoriesWindowManager instance;
    private StoriesWindow mStoriesDialog; //故事集dialog
    private StoriesAdapterView mStoriesAdapterView; //故事适配器控件
    private Window mWindow; //窗口
    private Context mContext;
    private List<CardInfo> mCardInfoList; //数据源，卡片数据列表

    public static StoriesWindowManager getInstance() {
        if (instance == null) {
            synchronized (StoriesWindowManager.class) {
                if (instance == null)
                    instance = new StoriesWindowManager();
            }
        }
        return instance;
    }

    protected StoriesWindowManager() {

    }

    /**
     * 初始化，设置windowmanager
     *
     * @param window
     * @return
     */
    public StoriesWindowManager init(Window window) {
        mWindow = window;
        return this;
    }

    /**
     * 显示故事集dialog
     *
     * @param context
     */
    public StoriesWindowManager showDialog(Context context, Window window, View parent) {
//        if (mWindow == null)
//            return;

        mContext = context;

        //点击的不是个人图标
        mStoriesDialog = new StoriesWindow(context);
        mStoriesDialog.init(window);
        mStoriesDialog.showAtLocation(parent, Gravity.CENTER, 0, 0);
        mStoriesDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onStoryWindowListener != null)
                    onStoryWindowListener.onDismiss();
            }
        });

        mStoriesAdapterView = (StoriesAdapterView) mStoriesDialog.getContentView().findViewById(R.id.stories_adapter_view);
        addData(context);
        return this;
    }

    /**
     * 使故事集消失
     */
    public void dismissDialog() {
        if (mStoriesDialog != null)
            mStoriesDialog.dismiss();
    }

    /**
     * 添加数据
     */
    private void addData(Context context) {
        if (mStoriesAdapterView == null)
            return;
        mCardInfoList = new ArrayList<>();
        int i;
        for (i = 0; i < 10; i++) {
            CardInfo cardInfo = new CardInfo();
            cardInfo.setTitle("最初不过你好" + i);
            cardInfo.setDetailPic("http://img.hb.aicdn.com/61588dbae333304cfe8510ac5183a33d30c922bf2ad93-kn7LXO_fw658");
            cardInfo.setExtract("最初不过你好，只是这世间所有斧砍刀削的相遇都不过起源于你好。");
            Date date = new Date(System.currentTimeMillis());
            date.setHours(date.getHours() - i);

            cardInfo.setCreateDate(date);
            UserInfo userInfo = new UserInfo();
            userInfo.setHeadPortrait("http://img4.duitang.com/uploads/item/201512/01/20151201084252_BmJzQ.jpeg");
            userInfo.setNickname("鹿人三千");
            cardInfo.setUserInfo(userInfo);
            mCardInfoList.add(cardInfo);
        }

        mCardInfoList.get(1).setDetailPic("https://img3.doubanio.com/lpic/s29059325.jpg");
        mCardInfoList.get(2).setDetailPic("http://img.hb.aicdn.com/493752fbda5ec3a183c39f0bc83d420557f8ac686c71c-kyaXbu_fw658");
        mCardInfoList.get(3).setDetailPic("http://img.hb.aicdn.com/03e819460466cad979f454cb001eb4e2c35f2611580ea-qkBa75_fw658");
        mCardInfoList.get(4).setDetailPic("http://img.hb.aicdn.com/df5dda0532822ab3f1317d6501ac818ee2d83c76685d6-WC54Ra_fw658");
        mCardInfoList.get(5).setDetailPic("http://img.hb.aicdn.com/e6762840e8ddbe594d38e607af4dc648aaff637e2f074-PkRUzx_fw658");
        mCardInfoList.get(6).setDetailPic("http://imgsrc.baidu.com/forum/w%3D580/sign=6132b5cc7fc6a7efb926a82ecdfaafe9/d650b0fb43166d22c96905874f2309f79052d236.jpg");
        mCardInfoList.get(7).setDetailPic("http://imgsrc.baidu.com/forum/w%3D580/sign=7bc5ce4dd81373f0f53f6f97940e4b8b/ae5ee2fe9925bc31f6e657ae57df8db1ca1370fc.jpg");
        mCardInfoList.get(8).setDetailPic("http://imgsrc.baidu.com/forum/pic/item/1a8ebe315c6034a841207a50c91349540b237648.jpg");
        mCardInfoList.get(9).setDetailPic("http://imgsrc.baidu.com/forum/w%3D580/sign=299acffcae86c91708035231f93d70c6/31ba9113b07eca80d96812e3982397dda04483f4.jpg");

        CardAdapter adapter = new CardAdapter(context, mCardInfoList);
        mStoriesAdapterView.init(adapter);
        mStoriesAdapterView.setOnCardSlidingListener(this);
        mStoriesAdapterView.setOnCardClickListener(this);
    }

    /**
     * 获取故事集弹窗的状态
     *
     * @return 是否正在显示
     */
    public boolean isShowing() {
        if (mStoriesDialog == null)
            return false;
        else
            return mStoriesDialog.isShowing();
    }

    private OnStoryWindowListener onStoryWindowListener;

    public StoriesWindowManager setOnStoryWindowListener(OnStoryWindowListener onStoryWindowListener) {
        this.onStoryWindowListener = onStoryWindowListener;
        return this;
    }

    @Override
    public void onCardLayouted(int position) {
        mStoriesAdapterView.requestLayout();
    }

    @Override
    public void onLeftIndex(int leftIndex) {
    }

    @Override
    public void OnCardClick(int cardIndex) {
        if (onStoryCardListener != null)
            onStoryCardListener.onCardClick(cardIndex, mCardInfoList.get(cardIndex));
    }

    /**
     * 窗框的显示回调函数
     */
    public interface OnStoryWindowListener {
        void onDismiss(); //窗框消失
    }

    private OnStoryCardListener onStoryCardListener;

    public void setOnStoryCardListener(OnStoryCardListener onStoryCardListener) {
        this.onStoryCardListener = onStoryCardListener;
    }

    /**
     * 故事卡片的点击等响应事件
     */
    public interface OnStoryCardListener {
        /**
         * 点击了卡片
         *
         * @param position      卡片的下标
         * @param clickCardInfo 卡片信息
         */
        void onCardClick(int position, CardInfo clickCardInfo);
    }

}
