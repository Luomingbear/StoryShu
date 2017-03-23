package com.storyshu.storyshu.imagepicker.control;

import android.app.Activity;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.storyshu.storyshu.imagepicker.adapter.AlbumAdapter;
import com.storyshu.storyshu.imagepicker.loader.AlbumLoader;
import com.storyshu.storyshu.imagepicker.model.Album;


/**
 * Created by Martin on 2017/1/17.
 */
public class AlbumController extends BaseLoaderController implements AdapterView.OnItemSelectedListener {

    private AlbumAdapter albumAdapter;
    private OnDirectorySelectListener directorySelectListener;
    private ListView listView;

    public void onCreate(Activity activity, ListView spinner,
                         OnDirectorySelectListener directorySelectListener) {
        super.onCreate(activity);
        this.albumAdapter = new AlbumAdapter(activity, null);
        this.directorySelectListener = directorySelectListener;
        listView = spinner;
        listView.setAdapter(albumAdapter);
        listView.setOnItemSelectedListener(this);
    }

    public void showAlbum() {

    }

    @Override
    protected int getLoaderId() {
        return ALBUM_LOADER_ID;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return AlbumLoader.newInstance(context);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        albumAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        albumAdapter.swapCursor(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void loadAlbums() {
        loaderManager.initLoader(getLoaderId(), null, this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (directorySelectListener != null) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            Album album = Album.valueOf(cursor);
            directorySelectListener.onSelect(album);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public interface OnDirectorySelectListener {
        void onSelect(Album album);

        void onReset(Album album);
    }

}
