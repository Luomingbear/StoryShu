package com.storyshu.storyshu.activity.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.mvp.create.article.CreateArticlePresenter;
import com.storyshu.storyshu.mvp.create.article.CreateArticlePresenterIml;
import com.storyshu.storyshu.mvp.create.article.CreateArticleView;
import com.storyshu.storyshu.utils.KeyBordUtil;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.text.RichTextEditor;
import com.storyshu.storyshu.widget.title.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建长故事
 * Created by bear on 2017/6/7.
 */

public class CreateArticleActivity extends IBaseActivity implements CreateArticleView, View.OnClickListener {

    private TitleView mTitleView;
    private EditText mTitleEdit; //标题输入
    private RichTextEditor mStoryEditView; //故事富文本编辑框
    private View mBottomLayout; //底部的工具栏
    private View mAddPicture; //添加图片
    private View mUndo; //撤销
    private View mRedo; //恢复

    private CreateArticlePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_long_story);

        initView();

        mPresenter = new CreateArticlePresenterIml(this, this);

        initEvent();

    }

    private void initView() {
        StatusBarUtils.setColor(CreateArticleActivity.this, R.color.colorRed);

        mTitleView = (TitleView) findViewById(R.id.title_view);

        mTitleEdit = (EditText) findViewById(R.id.title_edit);
        mStoryEditView = (RichTextEditor) findViewById(R.id.rich_text_edit);

        mBottomLayout = findViewById(R.id.bottom_layout);
        mAddPicture = findViewById(R.id.add_pic);
        mUndo = findViewById(R.id.undo);
        mRedo = findViewById(R.id.redo);

    }

    private void initEvent() {
        initTitle();

        mAddPicture.setOnClickListener(this);
        mUndo.setOnClickListener(this);
        mRedo.setOnClickListener(this);

        //
        mPresenter.initTitleEdit();
        mPresenter.initRichTextEditor();
    }

    private void initTitle() {
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                KeyBordUtil.hideKeyboard(CreateArticleActivity.this, mTitleView);
                onBackPressed();
            }

            @Override
            public void onCenterClick() {

            }

            @Override
            public void onCenterDoubleClick() {

            }

            @Override
            public void onRightClick() {
                mPresenter.issueLongStory();
            }
        });
    }


    @Override
    public void showToast(String s) {
        ToastUtil.Show(this, s);
    }

    @Override
    public void showToast(int stringRes) {
        ToastUtil.Show(this, stringRes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_pic:
                intent2ImagePicker();
                break;
            case R.id.undo:
                break;
            case R.id.redo:
                break;
        }
    }

    /**
     * 跳转到选择图片
     */
    private void intent2ImagePicker() {
        KeyBordUtil.hideKeyboard(this, mTitleView);

        ImagePicker.getInstance()
                .setMultiMode(true);
        intent2ImagePickActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQUEST_CODE_IMAGE) {
            List<ImageItem> list = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

            mStoryEditView.insertImage(list);
        }
    }

    @Override
    public void backActivity() {
        onBackPressed();
    }

    @Override
    public EditText getTitleEdit() {
        return mTitleEdit;
    }

    @Override
    public RichTextEditor getRichTextEditor() {
        return mStoryEditView;
    }

    @Override
    public View getBottomLayout() {
        return mBottomLayout;
    }
}
