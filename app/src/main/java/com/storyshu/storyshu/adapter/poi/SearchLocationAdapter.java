package com.storyshu.storyshu.adapter.poi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.LocationInfo;

import java.util.List;

/**
 * 搜索地点的列表显示recycleView 适配器
 * Created by bear on 2017/4/1.
 */

public class SearchLocationAdapter extends RecyclerView.Adapter<SearchLocationAdapter.ViewHold> {
    private Context mContext;
    private List<LocationInfo> mList;
    private OnSearchItemClickListener onSearchItemClickListener;

    public void setOnSearchItemClickListener(OnSearchItemClickListener onSearchItemClickListener) {
        this.onSearchItemClickListener = onSearchItemClickListener;
    }

    /**
     * 选项的点击回调
     */
    public interface OnSearchItemClickListener {
        void onGoClick(LocationInfo locationInfo);
    }

    public SearchLocationAdapter(Context mContext, List<LocationInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_location_item_layout, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        final LocationInfo info = mList.get(position);
        if (info == null)
            return;
        holder.title.setText(info.getTitle());
        holder.detail.setText(info.getDescribe());

        holder.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchItemClickListener != null)
                    onSearchItemClickListener.onGoClick(info);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        private TextView title; //位置的标题
        private TextView detail; //位置的详细地址
        private View go; //到这去的按钮

        public ViewHold(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.location_title);
            detail = (TextView) itemView.findViewById(R.id.location_detail);
            go = itemView.findViewById(R.id.go_here);
        }
    }
}
