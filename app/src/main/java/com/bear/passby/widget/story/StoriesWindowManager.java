package com.bear.passby.widget.story;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;

import com.bear.passby.R;
import com.bear.passby.adapter.CardAdapter;
import com.bear.passby.info.CardInfo;
import com.bear.passby.info.UserInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 故事集管家
 * Created by bear on 2016/12/4.
 */

public class StoriesWindowManager {
    private static StoriesWindowManager instance;
    private StoriesWindow mStoriesDialog; //故事集dialog
    private StoriesAdapterView mStoriesAdapterView; //故事适配器控件
    private Window mWindow;

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
        List<CardInfo> cardInfoList = new ArrayList<>();
        int i;
        for (i = 0; i < 10; i++) {
            CardInfo cardInfo = new CardInfo();
            cardInfo.setTitle("最初不过你好");
            cardInfo.setDetailPic("http://img.hb.aicdn.com/61588dbae333304cfe8510ac5183a33d30c922bf2ad93-kn7LXO_fw658");
            cardInfo.setExtract("最初不过你好，只是这世间所有斧砍刀削的相遇都不过起源于你好。");
            Date date = new Date(System.currentTimeMillis());
            cardInfo.setCreateDate(date);
            UserInfo userInfo = new UserInfo();
            userInfo.setHeadPortrait("http://img4.duitang.com/uploads/item/201512/01/20151201084252_BmJzQ.jpeg");
            userInfo.setNickname("鹿人三千");
            cardInfo.setUserInfo(userInfo);
            cardInfoList.add(cardInfo);
        }

        cardInfoList.get(1).setDetailPic("https://img3.doubanio.com/lpic/s29059325.jpg");
        cardInfoList.get(2).setDetailPic("https://img3.doubanio.com/lpic/s29132851.jpg");

        CardAdapter adapter = new CardAdapter(context, cardInfoList);
        mStoriesAdapterView.init(adapter);
    }

    private OnStoryWindowListener onStoryWindowListener;

    public StoriesWindowManager setOnStoryWindowListener(OnStoryWindowListener onStoryWindowListener) {
        this.onStoryWindowListener = onStoryWindowListener;
        return this;
    }

    /**
     * 窗框的显示回调函数
     */
    public interface OnStoryWindowListener {
        void onDismiss(); //窗框消失
    }

}
