package com.storyshu.storyshu.utils;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * AdapterView高度问题使用这个修复
 * Created by bear on 16/10/20.
 */

public class AdapterViewUtil {
    public static void FixHeight(AdapterView adapterView, Context context) {
        Adapter adapter = adapterView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, adapterView);
            //宽度为屏幕宽度
            int i1 = View.MeasureSpec.makeMeasureSpec(SysUtils.getScreenWidth(context.getApplicationContext()),
                    View.MeasureSpec.EXACTLY);
            //根据屏幕宽度计算高度
            int i2 = View.MeasureSpec.makeMeasureSpec(i1, View.MeasureSpec.UNSPECIFIED);
            listItem.measure(i1, i2);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = adapterView.getLayoutParams();
        params.height = totalHeight;
        if (adapterView instanceof ListView)
            params.height = totalHeight + (((ListView) adapterView).getDividerHeight() * adapter.getCount());
        adapterView.setLayoutParams(params);
    }


}
