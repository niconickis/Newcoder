package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    //用户注册方法
    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        //只判断了几种最简单的情况，自己补充
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        //用用户在注册/登录界面的输入账号在数据库被查询该ID是否存在
        User user = userDAO.selectByName(username);

        //账号已被注册
        if (user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }

        // 上面判断完毕，把注册用户加入数据库，密码强度
        user = new User();
        user.setName(username);
        //UUID:生成随机字符串，给用户加盐
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        //给注册用户生成一张随机的图片注入数据库
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        //设置密码，即将用户密码与盐合在一起生成保存在数据的密码。MD5算法在com.mowcoder.util中实现
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //下面再写注册（登陆）的页面响应，在LoginController里面
        
        //在UserService中调用TicketDao
        // 注册，给用户下发ticket（实际上只要注册成功或登陆成功就可以给用户一个ticket---人性化的操作）
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }
        //12.21继续看！！！！！

    // 用户登录服务
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }

        //把用户下发一个ticket,（首先要服务器存储了该用户，然后获取该用户的ID）用httpresponse来传递
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        // map里面存了所有信息但只有一个<k,v>
        return map;
    }
    //给登录用户增加一个ticket.先要设计ticket的dao层接口和model层
    // 实际上只需要一个userID就可以知道该用户的信息（之前要验证该用户是否存在）然后给它一个ticket
    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        // 给用户的设置的实际上是个统一的过期时间
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        // 用户登录后status状态设置为0（ticket状态有效）
        ticket.setStatus(0);
        //ticket的随机设置
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        // 把ticket字段加入数据库
        loginTicketDAO.addTicket(ticket);
        // 还要给用户也知道这个ticket，所以返回
        return ticket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
