package com.nowcoder.util;

/**
 * Created by nowcoder on 2016/7/30.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    // 增加关注服务的key，粉丝与关注对象!!!(逻辑上的理解)
    //粉丝
    private static String  BIZ_FOLLOWER = "FOLLOWER";
    // 关注对象
    private static String  BIZ_FOLLOWEE = "FOLLOWEE";


    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }

    // 所谓的BIZ_FOLLOWER实际上是拿到某项实体的所有粉丝（关注）（e.g我的粉丝、具体问题的粉丝、具体话题的粉丝），所以需要事务类型及其id.         我--本体被谁关注了
    public static String getBizFollwerKey(int entityType,int entityId){
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    
    // 而对于BIZ_FOLLOWEE来说，是想拿到某项实体的关注了什么（e.g 我的关注），当然问题是不能关注xxx的，所以这里的行为是用户行为（userId）,即用户关注某类实体的key
    // (e.g我关注的问题，我关注的人，我关注的评论，我的收藏。。。)                                                                               我--关注了谁
    public static String getBizFollweeKey(int userId,int entityType){
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

}
