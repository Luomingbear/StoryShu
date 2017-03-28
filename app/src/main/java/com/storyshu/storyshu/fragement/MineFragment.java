package com.storyshu.storyshu.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.storyshu.storyshu.R;

/**
 * 我的
 * Created by bear on 2017/3/13.
 */

public class MineFragment extends IBaseStatusFragment {
    @Override
    public int getLayoutRes() {
        return R.layout.mine_layout;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //状态栏
        setStatusBackgroundColor(R.color.colorGoldLight);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {

    }
}
