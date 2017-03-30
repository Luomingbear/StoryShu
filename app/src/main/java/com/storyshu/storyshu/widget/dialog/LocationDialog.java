package com.storyshu.storyshu.widget.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.amap.api.services.core.PoiItem;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.poi.PoiAdapter;
import com.storyshu.storyshu.utils.SysUtils;

import java.util.List;

/**
 * 选择位置的弹窗
 * Created by bear on 2017/3/29.
 */

public class LocationDialog extends IBaseDialog {
    private static final String TAG = "LocationDialog";
    private RecyclerView recyclerView;
    private OnLocationChooseListener onLocationChooseListener;

    /**
     * 设置数据并且显示
     *
     * @param poiList
     */
    public void setDataAndShow(final List<PoiItem> poiList, OnLocationChooseListener locationChooseListener) {
        this.onLocationChooseListener = locationChooseListener;
        PoiAdapter poiAdapter = new PoiAdapter(getContext(), poiList);
        Log.i(TAG, "setDataAndShow: 位置是！！！！！+" + poiList);
        poiAdapter.setPoiItemClickListener(new PoiAdapter.OnPoiItemClickListener() {
            @Override
            public void onClick(int position) {
                if (onLocationChooseListener != null)
                    onLocationChooseListener.onClick(poiList.get(position));
                dismiss();
            }
        });
        recyclerView.setAdapter(poiAdapter);
        show();

    }

    public void setOnLocationChooseListener(OnLocationChooseListener onLocationChooseListener) {
        this.onLocationChooseListener = onLocationChooseListener;
    }

    /**
     * 位置选择的回调
     */
    public interface OnLocationChooseListener {
        void onClick(PoiItem poiItem);
    }

    public LocationDialog(Context context) {
        super(context);
    }

    public LocationDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public LocationDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.choose_poi_layout;
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.choose_location_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void Create() {
        Display display = SysUtils.getScreenDisplay(getContext());
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (display.getWidth() * 0.85f);
        params.height = (int) (display.getHeight() * 0.85f);
        getWindow().setAttributes(params);
    }
}
