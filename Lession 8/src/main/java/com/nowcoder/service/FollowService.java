package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
// 如果一个用户要关注某样东西的时候两个操作：1.把自己放进对方的“粉丝”列表中去；2.将对方放到自己的关注列表中区
public class FollowService{

    @Autowired 
    JedisAdapter jedisAdapter;

    public boolean follow(int userId,int entityType,int entityId){
        // 把自己放进对方的“粉丝”列表中去
        String followerKey = RedisKeyUtil.getBizFollwerKey(entityType, entityId);
        // 将对方放到自己的关注列表中区
        String followeeKey = RedisKeyUtil.getBizFollweeKey(entityType, entityId);
        Date date = new Date();
        // 下面写zset方法，在jedisadapter中实现multi与excuse
        jedisAdapter.m...

    }
}