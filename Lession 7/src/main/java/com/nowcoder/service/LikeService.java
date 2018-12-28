package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService{
    @Autowired
    JedisAdapter jedisAdapter;

    // 如果某用户点了赞，就把userId加到LikeKey的set中，把他移除disLikeKey的set中：保持一致性。实际上对每一个评论（entityType与entityId）,都要维护两个set**********
    public long like(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));
        // 最后返回当前赞数
        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        // 如果用户点了踩，则对两个set的操作与上面的点赞行为相反
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
        // 最后返回当前赞数
        return jedisAdapter.scard(likeKey);
    }

    // 用户都某一评论的是否喜欢的状态
    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))){
            // 如果用户已经在likeKey的这个set中了，则对该问题应该高亮（1）
            return 1;
        }
        // 此外还要判断用户是否已经点了踩**********
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        // 如果点了踩返回-1，否则表示中立返回0
        return jedisAdapter.sismember(disLikeKey, String.valueOf(userId))? -1:0;
    }

    // 拿到某一评论的点赞的人数。这与上面用户的点赞返回相同，但他们面对的是不同的使用场景
    // 上面的用户踩赞面对的是用户点赞或踩之后的总赞数的更新；而下面的本方法实际上是用户无行为是的赞数的显示更新
    public long getLikeCount(int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);

    }
    // 下面需要在QuestionController中将问题及评论数据读出来以及写LikeController
}