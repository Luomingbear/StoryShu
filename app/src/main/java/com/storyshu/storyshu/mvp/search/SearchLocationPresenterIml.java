package com.storyshu.storyshu.mvp.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.storyshu.storyshu.mvp.base.IBasePresenter;

/**
 * Created by bear on 2017/4/1.
 */

public class SearchLocationPresenterIml extends IBasePresenter<SearchLocationView> implements SearchLocationPresenter {

    public SearchLocationPresenterIml(Context mContext, SearchLocationView mvpView) {
        super(mContext, mvpView);
    }

    @Override
    public void initSearchEdit() {
        mMvpView.getSearchEt().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void search() {

    }
}
