package com.nowcoder.dao;

import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    //xml版的配置，首先要在resources中有同名的QuestionDao.xml文件，传给该方法需要三个参数@Param将传入的参数解析mysql能看懂的col name（userId）、设置方法(offset以及limitde 的值)
    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);
        //question dao如果用上一个语句写的话要配置一个同名的xml文件，方法及名称都要相同
}
