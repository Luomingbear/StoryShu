package com.storyshu.storyshu.adapter.aoi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.base.IBaseAdapter;

import java.util.List;

/**
 * 选择当前位置的列表
 * 有兴趣点组成
 * Created by bear on 2016/12/26.
 */

public class PoiAdapter extends IBaseAdapter {
    private ViewHold mViewHold;

    public PoiAdapter(Context context, List<?> mList) {
        super(context, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHold = new ViewHold();
            convertView = getInflater().inflate(R.layout.aoiitem_layout, null);

            mViewHold.locationTextView = (TextView) convertView.findViewById(R.id.aoi_location_text);

            convertView.setTag(mViewHold);
        } else convertView.getTag();
        PoiItem poiItem;
        if (getItem(position) instanceof PoiItem) {
            poiItem = (PoiItem) getItem(position);
            mViewHold.locationTextView.setText(poiItem.getTitle());
        }
        return convertView;
    }

    private class ViewHold {
        TextView locationTextView; //位置信息显示的tv
    }
}
