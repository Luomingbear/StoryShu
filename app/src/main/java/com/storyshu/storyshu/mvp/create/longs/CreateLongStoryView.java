package com.storyshu.storyshu.mvp.create.longs;

import android.view.View;
import android.widget.EditText;

import com.storyshu.storyshu.mvp.view.base.IBaseView;
import com.storyshu.storyshu.widget.text.RichTextEditor;

/**
 * Created by bear on 2017/6/7.
 */

public interface CreateLongStoryView extends IBaseView {

    /**
     * 获取标题输入框
     *
     * @return
     */
    EditText getTitleEdit();

    /**
     * 获取富文本编辑器
     *
     * @return
     */
    RichTextEditor getRichTextEditor();

    /**
     * 获取底部的工具栏
     *
     * @return
     */
    View getBottomLayout();
}
