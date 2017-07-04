package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.bean.comment.CommentBean;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.ClickButton;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

import java.util.List;

/**
 * 评论项生成适配器
 * Created by bear on 2017/3/15.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHold> {
    private Context mContext;
    private List<CommentBean> mCommentList;
    private OnCommentClickListener onCommentClickListener;

    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }

    public CommentAdapter(Context mContext, List<CommentBean> mCommentList) {
        this.mContext = mContext;
        this.mCommentList = mCommentList;
    }

    @Override
    public int getItemViewType(int position) {
        return mCommentList == null ? 0 : mCommentList.get(position).getCommentType();
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case CommentBean.COMMENT:
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.comment_layout, parent, false);
                ViewHold viewHolder1 = new ViewHold(view1, viewType);
                return viewHolder1;
            case CommentBean.REPLY:
                View view2 = LayoutInflater.from(mContext).inflate(R.layout.reply_layout, parent, false);
                ViewHold viewHolder2 = new ViewHold(view2, viewType);
                return viewHolder2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {

        final CommentBean commentInfo = mCommentList.get(position);

        Glide.with(mContext).load(commentInfo.getUserInfo().getAvatar()).into(holder.avatar);

        //分割线
        if (position == 0)
            holder.line.setVisibility(View.GONE);

        switch (commentInfo.getCommentType()) {
            case CommentBean.COMMENT:
                holder.comment_content.setText(commentInfo.getComment());
                break;
            case CommentBean.REPLY:
                holder.comment_content.setText(commentInfo.getReplyUser() + ": " + commentInfo.getComment());
                holder.replay_content.setText(mContext.getString(R.string.reply_to, commentInfo.getReplyUser(), commentInfo.getReply()));
                break;
        }

        //标签
        holder.tag.setText(position + 1 + "#");

        holder.nickname.setText(commentInfo.getUserInfo().getNickname());
        holder.createTime.setText(TimeUtils.convertCurrentTime(mContext, commentInfo.getCreateTime()));

        holder.like.setNum(commentInfo.getLikeNum());
        holder.oppose.setNum(commentInfo.getOpposeNum());
    }


    @Override
    public int getItemCount() {
        return mCommentList == null ? 0 : mCommentList.size();
    }

    class ViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View line; //分割线
        private AvatarImageView avatar;
        private TextView nickname;
        private TextView createTime;
        private TextView tag;
        private ClickButton like;
        private ClickButton oppose;
        private TextView comment_content;

        private TextView replay_content;

        public ViewHold(View itemView, int type) {
            super(itemView);
            line = itemView.findViewById(R.id.line);
            avatar = (AvatarImageView) itemView.findViewById(R.id.avatar);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            createTime = (TextView) itemView.findViewById(R.id.create_time);
            like = (ClickButton) itemView.findViewById(R.id.like);
            oppose = (ClickButton) itemView.findViewById(R.id.oppose);
            tag = (TextView) itemView.findViewById(R.id.floor_tag);

            switch (type) {
                case CommentBean.COMMENT:
                    comment_content = (TextView) itemView.findViewById(R.id.comment_content);

                    break;
                case CommentBean.REPLY:
                    comment_content = (TextView) itemView.findViewById(R.id.comment_content);
                    replay_content = (TextView) itemView.findViewById(R.id.reply_content);
                    break;
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onCommentClickListener != null)
                onCommentClickListener.onClick(getLayoutPosition());
        }
    }

    public interface OnCommentClickListener {
        void onClick(int position);
    }

    /**
     * 类型
     */
    public enum Type {
        COMMENT,

        STORY
    }
}
