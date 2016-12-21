package com.storyshu.storyshu.tool.observable;

/**
 * 观察者
 * Created by bear on 2016/12/13.
 */
public interface EventObserver {
    void onNotify(Object sender, int eventId, Object... args);
}
