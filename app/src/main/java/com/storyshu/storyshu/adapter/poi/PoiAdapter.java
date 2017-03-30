package com.storyshu.storyshu.adapter.poi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.services.core.PoiItem;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.text.IBaseTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择当前位置的列表
 * 有兴趣点组成
 * Created by bear on 2016/12/26.
 */

public class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.ViewHold> implements View.OnClickListener {
    private static final String TAG = "PoiAdapter";
    private Context mContext;
    private List<PoiItem> mPoiItems; //数据源
    private OnPoiItemClickListener poiItemClickListener;

    public void setPoiItemClickListener(OnPoiItemClickListener poiItemClickListener) {
        this.poiItemClickListener = poiItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (poiItemClickListener != null)
            poiItemClickListener.onClick((Integer) v.getTag());
    }

    /**
     * 点击的回调
     */
    public interface OnPoiItemClickListener {
        void onClick(int position);
    }

    public PoiAdapter(Context context, List<PoiItem> poiList) {
        mPoiItems = new ArrayList<>();
        this.mContext = context;
        this.mPoiItems = poiList;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.poiitem_layout, parent, false);
        view.setOnClickListener(this);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        if (holder == null)
            return;

        if (position == mPoiItems.size() - 1)
            holder.line.setVisibility(View.GONE);

        holder.itemView.setTag(position);
        holder.locationTitle.setText(mPoiItems.get(position).getTitle());
//        holder.locationDetail.setText(mPoiItems.get(position).getBusinessArea());
    }

    @Override
    public int getItemCount() {
        return mPoiItems == null ? 0 : mPoiItems.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        IBaseTextView locationTitle; //位置的描述概要
        //        IBaseTextView locationDetail; //位置信息的详情
        View line; //分割线

        public ViewHold(View view) {
            super(view);

            this.locationTitle = (IBaseTextView) view.findViewById(R.id.location_title);
//            this.locationDetail = (IBaseTextView) view.findViewById(R.id.location_details);
            this.line = view.findViewById(R.id.line);
        }
    }
}
