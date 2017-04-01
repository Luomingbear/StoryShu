package com.storyshu.storyshu.mvp.airport;

import android.support.v7.widget.RecyclerView;

import com.storyshu.storyshu.info.AirPortPushInfo;
import com.storyshu.storyshu.mvp.view.base.IBaseView;

/**
 * mvp模式
 * 候机厅的View接口
 * Created by bear on 2017/3/20.
 */

public interface AirportView extends IBaseView {

    /**
     * 获取显示故事的rv控件
     *
     * @return
     */
    RecyclerView getPushRv();

    /**
     * 获取推送的故事信息
     */
    AirPortPushInfo getPushCardInfo();


}
