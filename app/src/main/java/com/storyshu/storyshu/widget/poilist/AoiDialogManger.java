package com.storyshu.storyshu.widget.poilist;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amap.api.services.core.PoiItem;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.aoi.PoiAdapter;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.widget.menu.MenuDialogManager;

import java.util.List;

/**
 * 兴趣点位置选择管家
 * Created by bear on 2016/12/26.
 */

public class AoiDialogManger {
    private static AoiDialogManger instance;

    private AoiDialog mAoiDialog; //兴趣点的弹窗
    private ListView mAoiList; //位置列表

    private Context mContext;

    public static AoiDialogManger getInstance() {
        if (instance == null) {

            synchronized (MenuDialogManager.class) {
                if (instance == null)
                    instance = new AoiDialogManger();
            }
        }
        return instance;
    }

    protected AoiDialogManger() {
    }

    /**
     * 显示菜单弹窗
     *
     * @param context
     */
    public AoiDialogManger showAoiDialog(Context context) {
        if (mAoiDialog != null && mAoiDialog.isShowing()) {
            return this;
        }

        mContext = context;
        mAoiDialog = new AoiDialog(context);
        mAoiDialog.setContentView(R.layout.choose_aoi_layout);
        mAoiDialog.show();
        initView();
        return this;
    }

    private void initView() {
        mAoiList = (ListView) mAoiDialog.findViewById(R.id.choose_aoi_list);

        final List<PoiItem> poiItemList = ILocationManager.getInstance().getPoiList();
        final PoiAdapter aoiAdapter = new PoiAdapter(mContext, poiItemList);
        mAoiList.setAdapter(aoiAdapter);
        mAoiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onPoiChooseListener != null)
                    onPoiChooseListener.onChoose(poiItemList.get(position));
            }
        });
    }

    /**
     * 让菜单弹窗消失
     */
    public void dismissAoiDialog() {
        if (mAoiDialog == null)
            return;
        mAoiDialog.dismiss();
    }

    /***
     * 菜单弹窗是否处于显示状态
     *
     * @return 是否处于显示状态
     */
    public boolean isShowing() {
        if (mAoiDialog == null)
            return false;
        return mAoiDialog.isShowing();
    }

    private OnPoiChooseListener onPoiChooseListener;

    public void setOnPoiChooseListener(OnPoiChooseListener onPoiChooseListener) {
        this.onPoiChooseListener = onPoiChooseListener;
    }

    public interface OnPoiChooseListener {
        void onChoose(PoiItem choosePoi);
    }
}
