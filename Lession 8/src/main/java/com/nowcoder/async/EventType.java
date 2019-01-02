package com.nowcoder.async;

/**
 * Created by nowcoder on 2016/7/30.
 */
// 用枚举定义handler
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    // 分号结束，括号中的值可以用EventTyoe.getDesc()获得
    MAIL(3);

    private int value;
    EventType(int value) { this.value = value; }
    public int getValue() { return value; }
}
