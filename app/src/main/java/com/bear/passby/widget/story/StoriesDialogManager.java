package com.bear.passby.widget.story;

import android.content.Context;

import com.bear.passby.R;
import com.bear.passby.adapter.CardAdapter;
import com.bear.passby.info.CardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 故事集管家
 * Created by bear on 2016/12/4.
 */

public class StoriesDialogManager {
    private static StoriesDialogManager instance;
//    private StoriesDialog mStoriesDialog; //故事集dialog
    private StoriesAdapterView mStoriesAdapterView; //故事适配器控件

    public static StoriesDialogManager getInstance() {
        if (instance == null) {
            synchronized (StoriesDialogManager.class) {
                if (instance == null)
                    instance = new StoriesDialogManager();
            }
        }
        return instance;
    }

    protected StoriesDialogManager() {

    }

    /**
     * 显示故事集dialog
     *
     * @param context
     */
    public void showDialog(Context context) {

        //点击的不是个人图标
        StoriesDialog mStoriesDialog = new StoriesDialog(context);
        mStoriesDialog.setContentView(R.layout.stories_dialog_layout);
        mStoriesDialog.show();

        mStoriesAdapterView = (StoriesAdapterView) mStoriesDialog.findViewById(R.id.stories_adapter_view);
        addData(context);
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
            cardInfoList.add(cardInfo);
        }

        CardAdapter adapter = new CardAdapter(context, cardInfoList);
        mStoriesAdapterView.init(adapter);
    }

}
