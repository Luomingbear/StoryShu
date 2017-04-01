package com.storyshu.storyshu.activity.story;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.mvp.search.SearchLocationPresenterIml;
import com.storyshu.storyshu.mvp.search.SearchLocationView;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.ToastUtil;

/**
 * 搜索地点的页面
 * Created by bear on 2017/4/1.
 */

public class SearchLocationActivity extends IBaseActivity implements SearchLocationView {
    private RecyclerView mLocationRv; //显示地点的Rv
    private EditText mSearchEdit; //搜索的地点编辑
    private View mSearchButton; //搜索的按钮

    private SearchLocationPresenterIml mSearchLocationPresenter; //搜索页面的控制

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.airport_layout);

        initView();

        mSearchLocationPresenter = new SearchLocationPresenterIml(this, this);
    }

    @Override
    public void initView() {
        //
        StatusBarUtils.setColor(this, R.color.colorBlack);
        //
        mSearchEdit = (EditText) findViewById(R.id.search_edit);

        mSearchButton = findViewById(R.id.search_btn);
        mSearchButton.setVisibility(View.VISIBLE);

        mLocationRv = (RecyclerView) findViewById(R.id.airport_push_list);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {
        //搜索按钮
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchLocationPresenter.search();
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
    public EditText getSearchEt() {
        return mSearchEdit;
    }

    @Override
    public RecyclerView getSearchItemRv() {
        return mLocationRv;
    }

    @Override
    protected void onDestroy() {
        mSearchLocationPresenter.distach();
        super.onDestroy();
    }
}
