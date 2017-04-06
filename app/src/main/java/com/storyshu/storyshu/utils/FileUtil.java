package com.storyshu.storyshu.utils;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件操作
 * Created by bear on 2017/4/6.
 */

public class FileUtil {

    /**
     * 复制文件到手机本地
     *
     * @param context
     * @param inName         assets里的文件名包含目录结构
     * @param strOutFileName 需要保存的地址
     * @throws IOException
     */
    public static void copyBigDataToSD(Context context, String inName, String strOutFileName) throws IOException {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFileName);
        myInput = context.getAssets().open(inName);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
}
