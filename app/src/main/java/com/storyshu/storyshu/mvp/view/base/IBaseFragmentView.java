package com.storyshu.storyshu.mvp.view.base;

/**
 * Created by bear on 2017/3/26.
 */

public interface IBaseFragmentView {
    /**
     * 弹窗显示
     *
     * @param s
     */
    void showToast(String s);

    /**
     * 弹窗显示
     *
     * @param stringRes
     */
    void showToast(int stringRes);
}
