package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by nowcoder on 2016/7/30.
 */
// 事件的入口，由它来将事件发给队列：用redis的Lists实现队列
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    // 把eventModel发出去，用队列保存事件
    public boolean fireEvent(EventModel eventModel) {
        try {
            // 两种方法作：1.BlockQueue<EventModel> q = new ArrayBlockingQueue<>(EventModel);2.redis的Lists;下面用第二种
            // 首先把当前事件转成JSON字符串加入队列，等到要处理的时候pop处理在转换成原事件，redis的队列是LIFO(lpush\lpop)（？？？？有点问题啊）
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            // 将键值推进redis中
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
