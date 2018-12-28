package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.controller.CommentController;
import com.nowcoder.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ListPosition;
import redis.clients.jedis.Tuple;

@Service
// 这个数据库在本网站开发项目中可以用在问题浏览数、赞踩功能的实现
public class JedisAdapter implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    // jedis的初始化
    private JedisPool pool;

    // public static void print(int index,Object obj) {
    //     System.out.println(String.format("%d,%s",index ,obj.toString()));


    // public static void main(String[] argv) {
    //     // 连接数据库
    //     Jedis jedis = new Jedis("redis://localhost:6379/9");
    //     jedis.flushDB();

    //     // 基本操作
    //     // 写入与读取(get\set)|改名字（key）

    //     jedis.set("hello","world");
    //     print(1,jedis.get("hello"));
    //     jedis.rename("hello", "newHello");
    //     print(1,jedis.get("newHello"));
    //     // 设置写入数据过期时间
    //     jedis.setex("hello2", 10, "world");
    //     // 上面的过期用在哪呢？用户登录注册的时候返回的请求中带有验证码输入表单，这时将验证码放到redis数据库中并设置过期时间，拿到用户的验证码输入之后与数据库中进行比对，
    //     // 超时则无效；同理包括发送验证码的短信，缓存系统也可以用redis?


    //     // 高并发的网页访问。比如秒杀活动，页面访问量的统计多放在内存中（也就是redis可以用的地方）
    //     // 数值操作

    //     jedis.set("pv","100");
    //     // 递增
    //     jedis.incr("pv");
    //     print(2,jedis.get("pv"));
    //     // 每次加5递增
    //     jedis.incrBy("pv", 5);
    //     print(2,jedis.get("pv"));
    //     // 减10递减
    //     jedis.decrBy("pv", 10);
    //     print(2,jedis.get("pv"));

    //     // 通配选取操作（类似正则）
    //     // 打印所有
    //     print(3,jedis.keys("*"));
    //     // 打印 n 开头的
    //     print(3,jedis.keys("n*"));
    //     print(3,jedis.keys("N*"));

    //     // 列表的创建与打印
    //     String listName = "list";
    //     jedis.del(listName);
    //     for (int i =0; i < 10; ++i){
    //         jedis.lpush(listName, "a"+ String.valueOf(i));
    //     }
    //     print(4,jedis.lrange(listName, 0, 5));
    //     print(4,jedis.lrange(listName, 0, 14));
    //     print(4,jedis.llen(listName));
    //     // 弹出最后加入的元素(但在表头)，LIFO
    //     print(4,jedis.lpop(listName));
    //     // 中间插入
    //     jedis.linsert(listName, ListPosition.BEFORE, "a4", "TAT");
    //     jedis.linsert(listName, ListPosition.AFTER, "a4", "QAQ");
    //     print(4,jedis.lrange(listName, 0, 12));


    //     // 哈希表，在那里用？：单条数据的扩展性，比如要增加某一列的数据，但大多数数据都不用该列的属性，只有及其个别数据才有该条属性的数据（稀疏列）。
    //     // 在redis可以随意定义这些属性（用hashset）加入数据库
    //     // hash
    //     // 这里实际上是 K:(k:v)的叠加hash. K：user1; k:name,age,phone; v:Tomas,18,2136...;
    //     String  userKey = "user1";
    //     jedis.hset(userKey, "name","Tomas");
    //     jedis.hset(userKey, "age","18");
    //     jedis.hset(userKey, "phone","21367813687");
    //     print(5,jedis.hget(userKey, "name"));
    //     print(5,jedis.hgetAll(userKey));
    //     jedis.hdel(userKey, "phone");
    //     print(5,jedis.hgetAll(userKey));
    //     print(5,jedis.hexists(userKey,"email"));
    //     print(5,jedis.hexists(userKey,"phone"));
    //     // 返回键名 和 值
    //     print(5,jedis.hkeys(userKey));
    //     print(5,jedis.hvals(userKey));
    //     //额外的增加
    //     jedis.hsetnx(userKey, "school", "seu");
    //     jedis.hsetnx(userKey, "age", "68");
    //     print(5,jedis.hgetAll(userKey));
    //     print(5,jedis.hget(userKey, "age"));


    //     // 集合（set）:天生的去重
    //     String likeKey1 = "commentLike1";
    //     String likeKey2 = "commentLike2";
    //     for(int i = 0; i < 10; ++i){
    //         jedis.sadd(likeKey1, String.valueOf(i));
    //         jedis.sadd(likeKey2, String.valueOf(i * i));
    //     }
    //     print(6,jedis.smembers(likeKey1));
    //     print(6,jedis.smembers(likeKey2));
    //     // 并集去重
    //     print(6,jedis.sunion(likeKey1,likeKey2));
    //     // 1中有而2中没有的,得到的结果是乱的
    //     print(6,jedis.sdiff(likeKey1,likeKey2));
    //     // 集合的交集
    //     print(6,jedis.sinter(likeKey1,likeKey2));
    //     print(6,jedis.sismember(likeKey1,"18"));
    //     // 删除某元素
    //     if (jedis.srem(likeKey1, "4") == 1){
    //         print(6,jedis.smembers(likeKey1));
    //     }else{
    //         print(6,"Error");
    //     }
    //     // 集合中的元素的迁移，加一个，减一个
    //     jedis.smove(likeKey2, likeKey1, "81");
    //     print(6,jedis.smembers(likeKey1));
    //     print(6,jedis.smembers(likeKey2));
    //     // 集合的大小
    //     print(6,jedis.scard(likeKey1));


    //     // 优先队列（堆）：用在哪？？排行榜、需要排序的的地方
    //     // 对应redis中的sorted sets
    //     String rankKey = "rankKey";
    //     jedis.zadd(rankKey, 18,"Jim");
    //     jedis.zadd(rankKey, 69,"Ben");
    //     jedis.zadd(rankKey, 99,"Lee");
    //     jedis.zadd(rankKey, 46,"Som");
    //     jedis.zadd(rankKey, 75,"Lucy");
    //     jedis.zadd(rankKey, 89,"Mei");
    //     print(7,jedis.zcard(rankKey));
    //     print(7,jedis.zcount(rankKey,89,100));
    //     print(7,jedis.zscore(rankKey,"Som"));
    //     jedis.zincrby(rankKey, 20, "Som");
    //     print(7,jedis.zscore(rankKey, "Som"));
    //     // zincrby可以直接返回分数
    //     print(7,jedis.zincrby(rankKey, 20, "Lucy"));
    //     // zrange(score从低到高)与zrevrange
    //     print(7,jedis.zrange(rankKey, 0, 2));
    //     print(7,jedis.zrevrange(rankKey, 0, 2));
    //     //分值与属性的取出.遍历(从低到高)
    //     for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "75", "100")){
    //         print(7,tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
    //     }
    //     // 显示个人排名（从0开始）
    //     print(7,jedis.zrank(rankKey, "Lucy"));
    //     print(7,jedis.zrevrank(rankKey, "Lucy"));

    //     // 分值相同时按字典序排序
    //     String setKey = "zset";
    //     jedis.zadd(setKey, 1,"a");
    //     jedis.zadd(setKey, 1,"b");
    //     jedis.zadd(setKey, 1,"c");
    //     jedis.zadd(setKey, 1,"d");
    //     jedis.zadd(setKey, 1,"e");
    //     jedis.zadd(setKey, 1,"f");
    //     // 按字典序计算数量
    //     print(8,jedis.zlexcount(setKey, "-", "+"));
    //     print(8,jedis.zlexcount(setKey, "(c", "+"));
    //     print(8,jedis.zlexcount(setKey, "[c", "+"));
    //     jedis.zrem(setKey, "f");
    //     print(8,jedis.zrange(setKey, 0, 10));
    //     // 按字典序删除元素
    //     jedis.zremrangeByLex(setKey, "(c", "+");
    //     print(8,jedis.zrange(setKey, 0, 10));
    //     // 按rank(索引位置)删除元素
    //     // jedis.zremrangeByRank(setKey,start,stop)


        // // 连接池的概念
        // // 默认是八个连接，用完要放回
        // JedisPool pool = new JedisPool();
        // for (int i = 0; i < 100; ++i){
        //     Jedis j = pool.getResource();
        //     print(45, j.get("pv"));
        //     // j用完要放回,这里有问！！！！？？？？
        //     j.close();;
        // }


    //     // 对用户对象写入redis的示例********************************************
    //     User user1 = new User();
    //     user1.setName("zhang");
    //     user1.setPassword("xxx");
    //     user1.setHeadUrl("a.png");
    //     user1.setSalt("salt");
    //     user1.setId(1);
    //     // 将user1对象及其中的数据序列化成JSON，toJSONString（）返回的是String:{"headUrl":"a.png","id":1,"name":"zhang","password":"xxx","salt":"salt"}
    //     print(9,JSONObject.toJSONString(user1));
    //     // 将序列化成json的user1对象及其中的数据以键值形式保存到redis数据库
    //     jedis.set("user1", JSONObject.toJSONString(user1));
    //     // 从数据库取出Json的string代表
    //     String value = jedis.get("user1");
    //     // 对取出的String进行反序列化得到User
    //     User user2 = JSON.parseObject(value, User.class);
    //     print(9,user2);

    //     // redis的GEO接口：开发距离最近的人的应用可用
    //     // 下面实现赞和踩的功能（set实现）


    //     // 思路，同mysql的调用一样，将redis中的set的各种方法在util层（即在本文件）包装(连接数据库的连接词)，然后将踩各功能写入service供controller调用
        
    //***************** */ 这里实际上一个集合的key就是一条comment的ID，value就是对其评论的用户id!!!!
    // 对一条评论，包括了增加用户id、删除id、统计id总数、判断是否在集合中；
    // 对应的业务实际上是：点赞、消除点赞（后悔了）、对一条评论赞的总数、判读用户是否已点赞（点赞就高亮）********************************

        @Override
        public void afterPropertiesSet() throws Exception{
            // pool连接数据库具体位置的初始化
            pool = new JedisPool("redis://localhost:6379/10");
        } 


        public long sadd(String key, String value) {
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                jedis.sadd(key,value);
            } catch (Exception e) {
                logger.error("发生异常" + e.getMessage());
            }
            finally{
                if (jedis != null){
                    jedis.close();
                }
            }
            // 表示正常加入
            return 0;          
        }

        public long scard(String key){
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                return jedis.scard(key);
            } catch (Exception e) {
                logger.error("发生异常" + e.getMessage());

            }
            finally{
                if (jedis != null){
                    jedis.close();
                }
            }
            return 0;
        }

        public long srem(String key,String value){
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                return jedis.srem(key, value);
            } catch (Exception e) {
                logger.error("发生异常" + e.getMessage());

            }finally{
                if (jedis != null){
                    jedis.close();
                }
            }
            return 0;
        }

        
        public Boolean sismember(String key,String value){
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                return jedis.sismember(key, value);
            } catch (Exception e) {
                logger.error("发生异常" + e.getMessage());

            }finally{
                if (jedis != null){
                    jedis.close();
                }
            }
            // 为什么是return false?因为try执行之后如果没问题就返回true了，但是仍要执行finally!!!!!!,如果最后try未执行成功（没有return），则最后应该返回false
            return false;
        }
        // 下面开始写LikeService与LikeControl
}


