package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.storyshu.storyshu.widget.photoview.PhotoView;

import java.util.List;

/**
 * 图片预览的viewpage适配器
 * Created by bear on 2017/3/30.
 */

public class PicturePreviewAdapter extends PagerAdapter {
    private List<String> mPictureList; //图片地址的列表
    private Context mContext;
    private OnPictureClickListener onPictureClickListener;

    public void setOnPictureClickListener(OnPictureClickListener onPictureClickListener) {
        this.onPictureClickListener = onPictureClickListener;
    }

    /**
     * 图片的点击事件
     */
    public interface OnPictureClickListener {
        void onClick();
    }

    public PicturePreviewAdapter(List<String> mPictureList, Context mContext) {
        this.mPictureList = mPictureList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mPictureList == null ? 0 : mPictureList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mContext);
        Glide.with(mContext).load(mPictureList.get(position)).into(photoView);
        container.addView(photoView);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPictureClickListener != null)
                    onPictureClickListener.onClick();
            }
        });
        return photoView;
    }
}

