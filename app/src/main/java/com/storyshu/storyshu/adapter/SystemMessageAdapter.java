package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.base.IBaseExpandableListAdapter;
import com.storyshu.storyshu.info.SystemMessageInfo;
import com.storyshu.storyshu.utils.time.TimeUtils;

import java.util.ArrayList;

/**
 * 系统信息的折叠显示适配器
 * Created by bear on 2017/3/21.
 */

public class SystemMessageAdapter extends IBaseExpandableListAdapter {
    public SystemMessageAdapter(Context mContext, ArrayList<?> mList) {
        super(mContext, mList);
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder mGroupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_group_layout,
                    parent, false);
            mGroupHolder = new GroupHolder();

            mGroupHolder.expandIcon = (ImageView) convertView.findViewById(R.id.expand_icon);
            mGroupHolder.groupTitle = (TextView) convertView.findViewById(R.id.message_text);
            mGroupHolder.groupIcon = (ImageView) convertView.findViewById(R.id.message_icon);
            convertView.setTag(mGroupHolder);
        } else mGroupHolder = (GroupHolder) convertView.getTag();

        if (getChildrenCount(0) == 0)
            return convertView;

        mGroupHolder.groupIcon.setBackgroundResource(R.drawable.message_computer);
        mGroupHolder.groupTitle.setText(R.string.system2me);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder mChildHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.system_message_item_layout,
                    parent, false);
            mChildHolder = new ChildHolder();

            mChildHolder.line = convertView.findViewById(R.id.line);
            mChildHolder.createTime = (TextView) convertView.findViewById(R.id.create_time);
            mChildHolder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(mChildHolder);
        } else mChildHolder = (ChildHolder) convertView.getTag();

        if (getChildrenCount(0) == 0)
            return convertView;

        if (getChild(groupPosition, childPosition) instanceof SystemMessageInfo) {
            if (childPosition == 0) {
                mChildHolder.line.setBackgroundResource(R.color.colorGrayLight);
            } else {
                mChildHolder.line.setBackgroundResource(R.drawable.dotted_line);
            }
            SystemMessageInfo info = (SystemMessageInfo) getChild(groupPosition, childPosition);
            mChildHolder.createTime.setText(TimeUtils.convertCurrentTime(getContext(), info.getCreateTime()));
            mChildHolder.content.setText(info.getDescribe());
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupHolder {
        ImageView groupIcon; //组别的图标
        TextView groupTitle; //组别的描述
        ImageView expandIcon; //折叠的图标
    }

    private class ChildHolder {
        TextView createTime; //发布时间
        TextView content; //内容
        View line; //分割线
    }

}
