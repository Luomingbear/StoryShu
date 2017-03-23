package com.storyshu.storyshu.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 列表工具
 * Created by bear on 2016/12/27.
 */

public class ListUtil {

    // 删除ArrayList中重复元素
    public static void removeDuplicate(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }

        list = null;
        list = newList;
    }
}
