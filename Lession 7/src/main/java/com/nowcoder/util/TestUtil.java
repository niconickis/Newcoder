package com.nowcoder.util;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


// 测试jedispoolde的运行
public class TestUtil {

    public static void print(int index,Object obj) {
        System.out.println(String.format("%d,%s",index ,obj.toString()));
    }

     public static void main(String[] argv) {
        //  Jedis jedis = new Jedis("redis://localhost:6379/9");
        // jedispool要完成连接数据库才可用
         JedisPool pool = new JedisPool("redis://localhost:6379/9");
        //  print(1,jedis.get("pv"));
         for (int i = 0;i < 20; ++i){
            Jedis jedis = pool.getResource();
            print(i,jedis.get("pv"));
            jedis.close();
         }
    }
}


     