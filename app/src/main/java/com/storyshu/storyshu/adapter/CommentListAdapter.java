package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.base.IBaseAdapter;
import com.storyshu.storyshu.info.CommentInfo;
import com.storyshu.storyshu.widget.ClickButton;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

import java.util.List;

/**
 * 评论的生成器
 * Created by bear on 2017/3/16.
 */

public class CommentListAdapter extends IBaseAdapter {
    private ViewHold viewHold;

    public CommentListAdapter(Context context, List<?> mList) {
        super(context, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.comment_layout, null);
            viewHold = new ViewHold();

            viewHold.avatar = (AvatarImageView) convertView.findViewById(R.id.avatar);
            viewHold.nickname = (TextView) convertView.findViewById(R.id.nickname);
            viewHold.createTime = (TextView) convertView.findViewById(R.id.create_time);
            viewHold.tag = (TextView) convertView.findViewById(R.id.floor_tag);
            viewHold.like = (ClickButton) convertView.findViewById(R.id.like);
            viewHold.oppose = (ClickButton) convertView.findViewById(R.id.oppose);
            viewHold.comment_content = (TextView) convertView.findViewById(R.id.comment_content);
            convertView.setTag(viewHold);
        } else viewHold = (ViewHold) convertView.getTag();

        CommentInfo commentInfo = (CommentInfo) getItem(position);

        ImageLoader.getInstance().displayImage(commentInfo.getAvatar(), viewHold.avatar);
        viewHold.nickname.setText(commentInfo.getNickname());
        viewHold.createTime.setText(commentInfo.getCreateTime());
        viewHold.tag.setText(commentInfo.getTags());
        viewHold.comment_content.setText(commentInfo.getComment());

        viewHold.like.setNum(commentInfo.getLikeNum());
        viewHold.oppose.setNum(commentInfo.getOpposeNum());

        return convertView;
    }

    private class ViewHold {
        private AvatarImageView avatar;
        private TextView nickname;
        private TextView createTime;
        private TextView tag;
        private ClickButton like;
        private ClickButton oppose;
        private TextView comment_content;

    }
}
