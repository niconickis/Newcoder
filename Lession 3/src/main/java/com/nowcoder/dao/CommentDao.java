package com.nowcoder.dao;

import java.util.List;

import com.nowcoder.model.Comment;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CommentDao{
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type ";
    String SELECT_FIELDS = " id " + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME," ( ",INSERT_FIELDS, ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType})"})
    int addComment(Comment comment);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id = #{id}"})
    Comment selectById(int id);
    

    @Update({"update ",TABLE_NAME," set content = #{content} "," where id = #{id}"})
    void updatePasswordById(Comment comment);
}