package com.nowcoder.async;
// 注释掉这个包了
// import sun.java2d.pipe.hw.AccelDeviceEventNotifier;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nowcoder on 2016/7/30.
 */
public class EventModel {
    private EventType type;
    // 触发事件载体（谁触发了该事件）
    private int actorId;
    // 触发了什么东西
    private int entityType;
    private int entityId;
    // 触发的东西是谁的（人与人的交互）
    private int entityOwnerId;

    // 触发时刻所要保留的信息，类似ViewObject,存储在exts中
    private Map<String, String> exts = new HashMap<String, String>();

    // 默认构造函数？？？？：我的理解是不是你自己构造的要传参数的构造器，这好像与alibba包中Jsonoobject方法的放射性质有关，即没有参数时候也要能够将对象变成JsonString
    public EventModel() {

    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        // 返回本类：即该事件及其关联的所有数据
        return this;
    }

    // 构造函数进来就识别event类型
    public EventModel(EventType type) {
        this.type = type;
    }

    public String getExt(String key) {
        return exts.get(key);
    }


    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
