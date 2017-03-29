package com.storyshu.storyshu.mvp.create;

import android.widget.TextView;

import com.storyshu.storyshu.info.LocationInfo;
import com.storyshu.storyshu.mvp.view.base.IBaseView;

import java.util.ArrayList;

/**
 * MVP模式
 * 写故事页面的View
 * Created by bear on 2017/3/17.
 */

public interface CreateStoryView extends IBaseView {
    /**
     * 获得故事的正文
     *
     * @return
     */
    String getStoryContent();

    /**
     * 选择配图
     *
     * @return
     */
    ArrayList<String> getStoryPic();

    /**
     * 故事的发布地点
     *
     * @return
     */
    LocationInfo getLocation();

    /**
     * 获取位置文字描述的控件
     *
     * @return
     */
    TextView getLocationTv();

    /**
     * 故事的生命周期
     *
     * @return 单位小时
     */
    int getLifeTime();

    /**
     * 获取故事的生命期的文本描述
     *
     * @return
     */
    TextView getLifeTimeTv();

    /**
     * 故事是否匿名
     *
     * @return
     */
    boolean isAnonymous();

    /**
     * 显示选择地点的弹窗
     */
    void showLocationDialog();

    /**
     * 显示图片选择器
     */
    void showPicSelector();

    /**
     * 添加图片预览到页面
     */
    void addPic2Layout();


    /**
     * 改变匿名按钮的状态
     */
    void changeAnonymous();

    /**
     * 返回
     */
    void backActivity();

    /**
     * 回到首页
     */
    void toMainActivity();
}
