package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.BaseUserInfo;
import com.storyshu.storyshu.model.UserModel;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.imageview.ChatAvatarView;

import java.util.List;

/**
 * 讨论的Rv适配器
 * Created by bear on 2017/5/25.
 */

public class DiscussAdapter extends RecyclerView.Adapter<DiscussAdapter.ViewHold> {
    private Context mContext;
    private List<EMMessage> mDiscussList; //聊天的信息数据列表
    private int mUserId = 0; //当前用户的id

    public static final int ME = 1; //显示我自己
    public static final int OTHER = ME + 1; //显示对方

    public DiscussAdapter(Context mContext, List<EMMessage> mDiscussList) {
        this.mContext = mContext;
        this.mDiscussList = mDiscussList;
        mUserId = ISharePreference.getUserId(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        String userID = String.valueOf(mUserId);
        if (mDiscussList.get(position).getFrom().equals(userID))
            return ME;
        else return OTHER;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHold viewHold;
        switch (viewType) {
            case ME:
                view = LayoutInflater.from(mContext).inflate(R.layout.discuss_me_layout, parent, false);
                viewHold = new ViewHold(view);
                return viewHold;
            case OTHER:
                view = LayoutInflater.from(mContext).inflate(R.layout.discuss_other_layout, parent, false);
                viewHold = new ViewHold(view);
                return viewHold;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHold holder, int position) {
        EMMessage discussItemInfo = mDiscussList.get(position);
        holder.avatar.setUserId(Integer.parseInt(discussItemInfo.getFrom()), new UserModel.OnUserInfoGetListener() {
            @Override
            public void onSucceed(BaseUserInfo userInfo) {
                holder.nickname.setText(userInfo.getNickname());

            }

            @Override
            public void onFailed(String error) {

            }
        });
        holder.content.setText(discussItemInfo.getBody().toString());


        /**
         * 信息距离上一条的时间小于一分钟，则不显示发布时间
         */
        if (position > 0 && TimeUtils.getTime(discussItemInfo.getMsgTime())
                .equals(TimeUtils.getTime(mDiscussList.get(position - 1).getMsgTime()))) {
            holder.createeTtime.setVisibility(View.GONE);
        } else {
            holder.createeTtime.setVisibility(View.VISIBLE);
            holder.createeTtime.setText(TimeUtils.convertCurrentTime(mContext, discussItemInfo.getMsgTime()));
        }
    }

    @Override
    public int getItemCount() {
        return mDiscussList == null ? 0 : mDiscussList.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        private ChatAvatarView avatar;
        private TextView nickname;
        private TextView content;
        private TextView createeTtime;

        public ViewHold(View itemView) {
            super(itemView);

            avatar = (ChatAvatarView) itemView.findViewById(R.id.avatar);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            content = (TextView) itemView.findViewById(R.id.content);
            createeTtime = (TextView) itemView.findViewById(R.id.create_time);
        }
    }
}
