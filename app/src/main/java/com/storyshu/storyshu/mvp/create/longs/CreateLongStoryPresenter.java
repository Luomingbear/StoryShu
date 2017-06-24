package com.storyshu.storyshu.mvp.create.longs;

/**
 * mvp
 * 编辑长文章
 * Created by bear on 2017/6/7.
 */

public interface CreateLongStoryPresenter {

    /**
     * 初始化标题输入框
     */
    void initTitleEdit();


    /**
     * 初始化富文本编辑器
     */
    void initRichTextEditor();

    /**
     * 发布长文章
     */
    void issueLongStory();
}
