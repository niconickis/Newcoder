package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
// 注释掉了这个包
// import sun.java2d.pipe.hw.AccelDeviceEventNotifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by nowcoder on 2016/7/30.
 */
// 将event分发给不同的Handler处理
// 为什么继承InitializingBean?:因为希望在@Autowired时候就初始化好类中的方法和属性,重写InitializingBean中的afterPropertiesSet() 方法,这是因为:
// spring初始化bean的时候，如果bean实现了InitializingBean接口，会自动调用afterPropertiesSet方法
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();

    // 新：applicationContext的作用
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 找到所有重写EventHandler的handler,返回bean names为键和值为eventHandler实例的map
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            // 两层遍历：外层为handler的遍历，里层为eventType的遍历，建立eventType和handler的映射关系
            // 一个一个取出：以集合的形式取出
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                // 找出每个handler关注的事件类型
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                // 将每个事件类型与handler通过map关联起来：config中的key为evevt type,value为handler
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        // tip:因为config的值为list,所以在加入新的键是，要初始化值为list
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    // 拿到值的列表后再用add加入新的handler
                    config.get(type).add(entry.getValue());
                }
            }
        }

        // 起线程找队列中的事件进行处理
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    // brpop方法使得redis队列变成了先进先出（FIFO）,把事件一一取出，但其返回的第一个String是该lists的key,要先去除
                    List<String> events = jedisAdapter.brpop(0, key);

                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }

                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }
    // tip：这里实际上只用了一个线程，可以起一个线程池来加快事件的处理
    // 下面就开始写各种各样的handler

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 没明白？？？？？？：this.applicationContext
        this.applicationContext = applicationContext;
    }
}
