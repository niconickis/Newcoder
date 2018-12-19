package com.nowcoder;

import com.nowcoder.dao.CommentDao;
import com.nowcoder.dao.MessageDao;
import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.model.Message;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@Sql("/init-schema.sql")//直接启动test下resources中的init-schema.sql？？？？没懂       
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;//直接IOC了

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    MessageDao messageDao;

    @Autowired
    CommentDao commentDao;

    @Test
    public void contextLoads() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));//随机设置一个标准化的headurl地址
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);//把一个user的数据加入到wenda.user表中去

            user.setPassword("newpassword");
            userDAO.updatePassword(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);//调用model中question类的方法
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("Balaababalalalal Content %d", i));
            questionDAO.addQuestion(question);//用question dao层插入一个问题的数据

            //现在我要添加几个站内信（message）和评论（comment）数据到这两张表中去，sql初始化的语句已经在init-scheme.sql中添加
            //先在model和Message类和Comment类，然后再建立MessageDao层和CommentDao层，把每个创建的数据通过dao层传到mysql数据库中去
            //先写了一个MessageDao和一个model Message,下面先将message数据写入数据库
            Message message  = new Message();
            message.setContent(String.format("ladygaga:%d", i));
            message.setConversationId(i);
            message.setFromid(i * i);
            message.setToid(i * i + i);
            Date dateNew = new Date();
            dateNew.setTime(date.getTime() + 1000 *5 * 3600 * i);
            message.setCreatedDate(dateNew);
            messageDao.addMessage(message);
            //插入成功了，但是怎么用xml文件配置复杂的sql语句与mybatis的映射还是不会，明天的工作：1.把comment在仿照前面的写一遍；2.用xml写复杂的mapper映射语句；3.完成两个网页的设计

            //将Comment的数据注入到数据库
            Comment comment = new Comment();
            //comment.setId(i);
            comment.setContent(String.format("yoyoyoyo:%d", i+2));
            comment.setEntityId(i+116);
            comment.setEntityType("String");
            comment.setUserId(i*i);
            Date dateNew2 = new Date();
            dateNew2.setTime(date.getTime() + 1000 * 3600 * 12 * i);
            comment.setCreatedDate(dateNew2);
            commentDao.addComment(comment);
            
            //下面看com.nowcoder.dao中的QuestionDao.xml:mybatis中通过xml文件来映射复杂的mysql语句
            //做了一个相同的MessageDao.xml用来选择最新的站内信
            //下面开始用数据库的数据替换网页内容
        }

        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));
    }
}
