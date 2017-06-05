package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.DiscussItemInfo;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

import java.util.List;

/**
 * 讨论的Rv适配器
 * Created by bear on 2017/5/25.
 */

public class DiscussAdapter extends RecyclerView.Adapter<DiscussAdapter.ViewHold> {
    private Context mContext;
    private List<DiscussItemInfo> mDiscussList; //聊天的信息数据列表
    private int mUserId = 0; //当前用户的id

    public DiscussAdapter(Context mContext, List<DiscussItemInfo> mDiscussList) {
        this.mContext = mContext;
        this.mDiscussList = mDiscussList;
        mUserId = ISharePreference.getUserId(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return mDiscussList.get(position).getDiscussType();
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHold viewHold;
        switch (viewType) {
            case DiscussItemInfo.ME:
                view = LayoutInflater.from(mContext).inflate(R.layout.discuss_me_layout, parent, false);
                viewHold = new ViewHold(view);
                return viewHold;
            case DiscussItemInfo.OTHER:
                view = LayoutInflater.from(mContext).inflate(R.layout.discuss_other_layout, parent, false);
                viewHold = new ViewHold(view);
                return viewHold;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        DiscussItemInfo discussItemInfo = mDiscussList.get(position);
        Glide.with(mContext).load(discussItemInfo.getUserInfo().getAvatar()).into(holder.avatar);
        holder.nickname.setText(discussItemInfo.getUserInfo().getNickname());
        holder.content.setText(discussItemInfo.getContent());


        /**
         * 信息距离上一条的时间小于一分钟，则不显示发布时间
         */
        if (position > 0 && TimeUtils.getTime(discussItemInfo.getCreateTime())
                .equals(TimeUtils.getTime(mDiscussList.get(position - 1).getCreateTime()))) {
            holder.createeTtime.setVisibility(View.GONE);
        } else {
            holder.createeTtime.setVisibility(View.VISIBLE);
            holder.createeTtime.setText(TimeUtils.convertCurrentTime(mContext, discussItemInfo.getCreateTime()));
        }
    }

    @Override
    public int getItemCount() {
        return mDiscussList == null ? 0 : mDiscussList.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        private AvatarImageView avatar;
        private TextView nickname;
        private TextView content;
        private TextView createeTtime;

        public ViewHold(View itemView) {
            super(itemView);

            avatar = (AvatarImageView) itemView.findViewById(R.id.avatar);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            content = (TextView) itemView.findViewById(R.id.content);
            createeTtime = (TextView) itemView.findViewById(R.id.create_time);
        }
    }
}
