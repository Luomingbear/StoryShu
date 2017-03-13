package com.storyshu.storyshu.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮箱格式验证
 * Created by bear on 2017/3/11.
 */

public class EmailFormatCheckUtil {

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }
}
