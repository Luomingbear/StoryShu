package com.storyshu.storyshu.utils;

import java.util.List;

/**
 * 列表工具
 * Created by bear on 2016/12/27.
 */

public class ListUtil {

    // 删除ArrayList中重复元素
    public static void removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
    }
}
