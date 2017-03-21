package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.base.IBaseExpandableListAdapter;
import com.storyshu.storyshu.info.StoryMessageInfo;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

import java.util.ArrayList;

/**
 * 折叠列表的适配器
 * Created by bear on 2017/3/21.
 */

public class MessageExpandableAdapter extends IBaseExpandableListAdapter {
    public MessageExpandableAdapter(Context mContext, ArrayList<?> mList) {
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

        if (getChild(groupPosition, 0) instanceof StoryMessageInfo) {
            StoryMessageInfo storyMessageInfo = (StoryMessageInfo) (getList().get(0));

            switch (storyMessageInfo.getMessageType()) {
                case LIKE:
                    mGroupHolder.groupIcon.setBackgroundResource(R.drawable.message_mylove);
                    mGroupHolder.groupTitle.setText(R.string.like2me);
                    break;
                case COMMENT:
                    mGroupHolder.groupIcon.setBackgroundResource(R.drawable.message_comment);
                    mGroupHolder.groupTitle.setText(R.string.comment2me);
                    break;
                case SYSTEM:
                    mGroupHolder.groupIcon.setBackgroundResource(R.drawable.message_computer);
                    mGroupHolder.groupTitle.setText(R.string.system2me);
                    break;
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder mChildHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_child_layout,
                    parent, false);
            mChildHolder = new ChildHolder();

            mChildHolder.line = convertView.findViewById(R.id.line);
            mChildHolder.avatar = (AvatarImageView) convertView.findViewById(R.id.avatar);
            mChildHolder.nickname = (TextView) convertView.findViewById(R.id.nickname);
            mChildHolder.createTime = (TextView) convertView.findViewById(R.id.create_time);
            mChildHolder.content = (TextView) convertView.findViewById(R.id.content);
            mChildHolder.cover = (ImageView) convertView.findViewById(R.id.cover);
            mChildHolder.extract = (TextView) convertView.findViewById(R.id.extract);
            convertView.setTag(mChildHolder);
        } else mChildHolder = (ChildHolder) convertView.getTag();

        if (getChild(groupPosition, childPosition) instanceof StoryMessageInfo) {
            StoryMessageInfo messageInfo = (StoryMessageInfo) getChild(groupPosition, childPosition);
            //分割线第一个元素的分割线是实体的
            if (childPosition == 0)
                mChildHolder.line.setBackgroundResource(R.color.colorGrayLight);
            else {
                mChildHolder.line.setBackgroundResource(R.drawable.dotted_line);
            }
            ImageLoader.getInstance().displayImage(messageInfo.getUserInfo().getAvatar(), mChildHolder.avatar);
            mChildHolder.nickname.setText(messageInfo.getUserInfo().getNickname());
            mChildHolder.createTime.setText(TimeUtils.convertCurrentTime(getContext(),
                    messageInfo.getCreateTime()));
            switch (messageInfo.getMessageType()) {
                case LIKE:
                    mChildHolder.content.setText(R.string.like_my_story);
                    break;
                case COMMENT:
                    mChildHolder.content.setText(messageInfo.getComment());
                    break;
                case SYSTEM:
                    break;
            }
            if (TextUtils.isEmpty(messageInfo.getCover())) {
                mChildHolder.extract.setVisibility(View.VISIBLE);
                mChildHolder.cover.setVisibility(View.GONE);
                mChildHolder.extract.setText(messageInfo.getStoryContent());
            } else {
                mChildHolder.extract.setVisibility(View.GONE);
                mChildHolder.cover.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(messageInfo.getCover(), mChildHolder.cover);
            }
        }
        return convertView;
    }

    private class GroupHolder {
        ImageView groupIcon; //组别的图标
        TextView groupTitle; //组别的描述
        ImageView expandIcon; //折叠的图标
    }

    private class ChildHolder {
        View line; //分割线
        AvatarImageView avatar;
        TextView nickname;
        TextView createTime;
        TextView content; //用户的行为 ，赞了你 还是评论了
        ImageView cover;
        TextView extract; //右边的纯文本故事信息展示
    }
}
