package com.storyshu.storyshu.mvp.main;

import com.storyshu.storyshu.mvp.view.base.IBaseView;
import com.storyshu.storyshu.widget.BottomNavigationBar;

/**
 * mvp模式
 * 主页的View
 * Created by bear on 2017/5/13.
 */

public interface MainView extends IBaseView {
    BottomNavigationBar getBottomNavigationBar();

    void checkStorgePermission();
}
