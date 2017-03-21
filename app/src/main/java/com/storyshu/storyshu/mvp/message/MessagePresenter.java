package com.storyshu.storyshu.mvp.message;

/**
 * mvp模式
 * 消息页面的代理人接口
 * Created by bear on 2017/3/21.
 */

public interface MessagePresenter {
    /**
     * 列表的类型
     */
    enum ListType {
        LIKE,

        COMMENT,

        COMPUTER
    }

    /**
     * 获取信息列表数据；
     */
    void getMessageData();

    /**
     * 显示信息列表
     */
    void showMessageList();

    /**
     * 展开列表
     */
    void unFoldList(ListType listType);

    /**
     * 折叠列表
     */
    void FoldList(ListType listType);

    /**
     * 跳转到故事屋
     */
    void toStoryRoom();

    /**
     * 跳转系统信息详情
     */
    void toComputerMessage();
}
