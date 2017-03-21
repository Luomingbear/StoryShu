package com.storyshu.storyshu.adapter.base;

import android.content.Context;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

/**
 * 折叠的listView的适配器基类
 * 只是用于只有一个组
 * Created by bear on 2017/3/21.
 */

public abstract class IBaseExpandableListAdapter extends BaseExpandableListAdapter {
    private ArrayList<?> mList;
    private Context mContext;

    public ArrayList<?> getList() {
        return mList;
    }

    public Context getContext() {
        return mContext;
    }

    public IBaseExpandableListAdapter(Context mContext, ArrayList<?> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return mList == null ? 0 : 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList == null ? null : mList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mList == null ? 0 : 1;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mList == null ? 0 : childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
