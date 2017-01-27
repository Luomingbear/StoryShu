package com.storyshu.storyshu.widget.poilist;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amap.api.services.core.PoiItem;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.poi.PoiAdapter;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.widget.inputview.CustomLocationEdit;

import java.util.List;

/**
 * 兴趣点位置选择管家
 * Created by bear on 2016/12/26.
 */

public class PoiDialogManger {
    private static final String TAG = "PoiDialogManger";
    private static PoiDialogManger instance;

    private PoiDialog mPoiDialog; //兴趣点的弹窗
    private ListView mPoiListView; //位置列表

    private Context mContext;

    public static PoiDialogManger getInstance() {
        if (instance == null) {
            synchronized (PoiDialogManger.class) {
                if (instance == null)
                    instance = new PoiDialogManger();
            }
        }
        return instance;
    }

    protected PoiDialogManger() {
    }

    /**
     * 显示菜单弹窗
     *
     * @param context
     */
    public PoiDialogManger showAoiDialog(Context context) {
        if (mPoiDialog != null && mPoiDialog.isShowing()) {
            return this;
        }

        mContext = context;
        mPoiDialog = new PoiDialog(context);
        mPoiDialog.setContentView(R.layout.choose_poi_layout);
        mPoiDialog.show();
        initView();
        return this;
    }

    private void initView() {
        mPoiListView = (ListView) mPoiDialog.findViewById(R.id.choose_location_list);

        /**
         * 开始搜索
         */
        final List<PoiItem> mPoiItemList = ILocationManager.getInstance().getPoiList();
        PoiAdapter aoiAdapter = new PoiAdapter(mContext, mPoiItemList);
        mPoiListView.setAdapter(aoiAdapter);

        CustomLocationEdit customLocationEdit = new CustomLocationEdit(mContext);
        mPoiListView.addFooterView(customLocationEdit);
        customLocationEdit.setOnCustomLocationEditListener(new CustomLocationEdit.OnCustomLocationEditListener() {
            @Override
            public void OnCustomLocation(String locationName) {
                if (mPoiItemList == null || mPoiItemList.size() == 0)
                    return;

                PoiItem p = mPoiItemList.get(0);
                PoiItem poi = new PoiItem(p.getPoiId(), p.getLatLonPoint(), locationName, p.getSnippet());
                if (onPoiChooseListener != null)
                    onPoiChooseListener.onChoose(poi);
            }
        });

        /**
         * 点击时间监听
         */
        mPoiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: choose position:" + position);
                if (onPoiChooseListener != null)
                    onPoiChooseListener.onChoose(mPoiItemList.get(position));
            }
        });
    }

    /**
     * 让菜单弹窗消失
     */
    public void dismissAoiDialog() {
        if (mPoiDialog == null)
            return;
        mPoiDialog.dismiss();
    }

    /***
     * 菜单弹窗是否处于显示状态
     *
     * @return 是否处于显示状态
     */
    public boolean isShowing() {
        if (mPoiDialog == null)
            return false;
        return mPoiDialog.isShowing();
    }

    private OnPoiChooseListener onPoiChooseListener;

    public void setOnPoiChooseListener(OnPoiChooseListener onPoiChooseListener) {
        this.onPoiChooseListener = onPoiChooseListener;
    }

    public interface OnPoiChooseListener {
        void onChoose(PoiItem choosePoi);
    }
}
