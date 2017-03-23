package com.storyshu.storyshu.imagepicker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.imagepicker.model.Album;
import com.storyshu.storyshu.imagepicker.util.UriUtil;


/**
 * Created by Martin on 2017/1/17.
 */
public class AlbumAdapter extends CursorAdapter {

    private final LayoutInflater layoutInflater;

    public AlbumAdapter(Context context, Cursor c) {
        super(context, c, false);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.album_item_layout, parent, false);
        AlbumViewHolder albumViewHolder = new AlbumViewHolder(view);
        view.setTag(albumViewHolder);
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (!getCursor().moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item_layout,
                    parent, false);
        }
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AlbumViewHolder viewHolder = (AlbumViewHolder) view.getTag();
        Album album = Album.valueOf(cursor);

        String img_path = UriUtil.getRealPathFromUri(context, album.buildCoverUri());
        ImageLoader.getInstance().displayImage("file://" + img_path,
                viewHolder.albumCover);
        viewHolder.albumTitle.setText(album.getDisplayName() + "(" + album.getCount() + ")");
    }

    static class AlbumViewHolder {
        ImageView albumCover;
        TextView albumTitle;

        public AlbumViewHolder(View itemView) {
            albumCover = (ImageView) itemView.findViewById(R.id.album_cover);
            albumTitle = (TextView) itemView.findViewById(R.id.album_name);
        }
    }
}
