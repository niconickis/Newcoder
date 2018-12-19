package com.nowcoder.dao;

import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Mapper//表示与mybatis关联，dao目录下文件专门用与数据库的关联，下面这个接口与数据库是映射的（mapper）
public interface UserDAO {//用于接收数据库中wenda.user表的接口，供给上层的service（UserService.java用）
    //注意空格
    //将要读取的mysql数据库的表中的字段独立出来，好处是下面使用的多处sql语句如果列名要改变的话只要改变这里就行啦
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";//这里的标记仍然是mysql中的列名
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    //通过注解的方式（@）写mysql语句;#{name}表示变量  
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,   //注意空格 
            ") values (#{name},#{password},#{salt},#{headUrl})"})  //mysql语句的应用，这里head_url变成了headUrl,因为这里实际上是通过User.java这个容器将数据插入到mysql的wenda.user表中去的
    int addUser(User user);//将UserDao的方法与mysql中的插入语句相映射，#{name},#{password},#{salt},#{headUrl}这些变量是model中user的变量

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})//怎么dao层的变量都是模板语言
    User selectById(int id);//这里就不需要传入user了，因为在service层会传入一个ID以供查询

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);
}
