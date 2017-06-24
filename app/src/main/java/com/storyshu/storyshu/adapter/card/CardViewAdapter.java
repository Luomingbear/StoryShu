package com.storyshu.storyshu.adapter.card;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.CardInfo;

import java.util.List;

/**
 * 滑动rv的测试adapter
 * Created by bear on 2017/6/5.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHold> {
    private List<CardInfo> mList;
    private Context mContext;

    public CardViewAdapter(List<CardInfo> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_view_layout, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {
        public ViewHold(View itemView) {
            super(itemView);
        }
    }
}
