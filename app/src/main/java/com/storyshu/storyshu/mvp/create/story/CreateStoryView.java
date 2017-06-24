package com.storyshu.storyshu.mvp.create.story;

import android.widget.TextView;

import com.storyshu.storyshu.mvp.view.base.IBaseActivityView;

import java.util.List;

/**
 * MVP模式
 * 写故事页面的View
 * Created by bear on 2017/3/17.
 */

public interface CreateStoryView extends IBaseActivityView {
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
    List<String> getStoryPic();


    /**
     * 获取位置文字描述的控件
     *
     * @return
     */
    TextView getLocationTv();

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
    Boolean isAnonymous();

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
}
