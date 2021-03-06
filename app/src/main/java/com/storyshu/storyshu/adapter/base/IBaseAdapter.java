package com.storyshu.storyshu.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 基本适配器
 * Created by bear on 2016/12/4.
 */

public abstract class IBaseAdapter extends BaseAdapter {
    private List<?> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public IBaseAdapter(Context context, List<?> mList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    public Context getContext() {
        return mContext;
    }

    public List<?> getList() {
        return mList;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    @Override
    public long getItemId(int position) {
        return mList == null ? 0 : position;
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }
}
