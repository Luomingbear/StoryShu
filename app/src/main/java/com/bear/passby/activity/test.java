package com.bear.passby.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.bear.passby.R;

/**
 * test
 * Created by bear on 2016/12/6.
 */

public class test extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {

    }
}
