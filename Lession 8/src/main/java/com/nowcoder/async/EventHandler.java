package com.nowcoder.async;

import java.util.List;

/**
 * Created by nowcoder on 2016/7/30.
 */
// lists中的事件（event）pop出来，要经过EventHandler,所以每个事件要与不同的eventHandler建立不同的映射（即该事件要经过那几个具体的handler来处理）
// EventHandler核心的两个接口：getSupportEventTypes：即该EventHandler关注哪几种类型的事件（event），当pop出来的是这几种类型的时候，调用doHandle处理事件
// 所以应该很多很多种handle来处理不同的event(继承此接口)
// 下面讲eventConsemer
public interface EventHandler {
    void doHandle(EventModel model);

    List<EventType> getSupportEventTypes();
}
