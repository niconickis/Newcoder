package com.nowcoder.dao;

import java.util.List;

import com.nowcoder.model.Message;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import ch.qos.logback.classic.db.names.TableName;

@Mapper
public interface MessageDao{
    String TABLE_NAME = " message ";
    String INSERT_FIELDS  = " fromid, toid, content, conversation_id, created_date ";
    String SELECT_FIELDS  = " id, " + INSERT_FIELDS;
    
    @Insert({"insert into ",TABLE_NAME," ( ", INSERT_FIELDS ,")"," values (#{fromid},#{toid},#{content},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    // @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where"," id = #{id}"})
    // Message selectByID(int id);
    //自己写的关于用xml配置dao层的方法
    List<Message> selectLatestMessages(@Param("fromid") int fromid,@Param("offset") int offset,@Param("limit") int limit);

    @Update({"update ",TABLE_NAME," set fromid = #{fromid} "," where ","id = #{id}"})
    Void updateById(Message message);//这里需要从设置Message 的值来给数据库传值
}
