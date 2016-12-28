package com.storyshu.storyshu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.text.RichTextEditor;

/**
 * Test
 * Created by bear on 2016/12/6.
 */

public class Test extends FragmentActivity implements View.OnClickListener {
    private RichTextEditor richTextEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        richTextEditor = (RichTextEditor) findViewById(R.id.rich_editor);


        findViewById(R.id.add_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richTextEditor.insertImage("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1482231789918.jpg");
            }
        });
    }

    @Override
    public void onClick(View v) {
    }
}
