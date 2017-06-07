package com.storyshu.storyshu.mvp.create.longs;

import android.content.Context;
import android.view.View;

import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.widget.text.RichTextEditor;

/**
 * mvp
 * 创建文章的逻辑实现
 * Created by bear on 2017/6/7.
 */

public class CreateLongStoryPresenterIml extends IBasePresenter<CreateLongStoryView> implements CreateLongStoryPresenter {

    public CreateLongStoryPresenterIml(Context mContext, CreateLongStoryView mvpView) {
        super(mContext, mvpView);
    }


    @Override
    public void initTitleEdit() {
        mMvpView.getTitleEdit().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.equals(mMvpView.getTitleEdit())) {
                    mMvpView.getBottomLayout().setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void initRichTextEditor() {
        //获取到焦点就显示工具栏
        mMvpView.getRichTextEditor().setOnTextFocusListener(new RichTextEditor.OnTextFocusListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mMvpView.getBottomLayout().setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
