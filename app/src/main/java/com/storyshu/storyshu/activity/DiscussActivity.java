package com.storyshu.storyshu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.mvp.discuss.DisscussView;
import com.storyshu.storyshu.utils.KeyBordUtil;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.widget.text.RoundTextView;
import com.storyshu.storyshu.widget.title.TitleView;

/**
 * 讨论页面
 * Created by bear on 2017/5/24.
 */

public class DiscussActivity extends IBaseActivity implements DisscussView {
    private TitleView mTitleView;
    private RecyclerView mRecyclerView; //讨论记录的Rv
    private SwipeRefreshLayout mRefreshLayout; //下拉刷新
    private EditText mEditText; //文本输入
    private RoundTextView mSend; //发送

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_discuss_layout);
        initView();

        initEvent();
    }

    private void initView() {
        StatusBarUtils.setColor(DiscussActivity.this, R.color.colorRed);

        mTitleView = (TitleView) findViewById(R.id.title_view);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.listView);

        mEditText = (EditText) findViewById(R.id.input_edit);

        mSend = (RoundTextView) findViewById(R.id.send);

    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
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
            }
        });

    }

    /**
     * 初始化输入框
     */
    private void initEditText() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    mSend.setBgColor(R.color.colorRed);
                else
                    mSend.setBgColor(R.color.colorGrayLight);
            }
        });

        findViewById(R.id.hide_keyboard_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBordUtil.hideKeyboard(DiscussActivity.this, mEditText);
            }
        });
    }

    private void initEvent() {
        initTitle();

        initEditText();

    }

    @Override
    public void showToast(String s) {

    }

    @Override
    public void showToast(int stringRes) {

    }
}
