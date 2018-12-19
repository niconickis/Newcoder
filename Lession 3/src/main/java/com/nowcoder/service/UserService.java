package com.nowcoder.service;

import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
//service的作用：连接controller和dao，该文件下是通过该类读取dao层连接的数据库。UserService就是通过Userdao调用数据库中的wenda.user表的数据
//是加上就是三层模型：dao层调用数据库；service调用dao层；controlller调用service层
/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO; //dao层的依赖注入

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
