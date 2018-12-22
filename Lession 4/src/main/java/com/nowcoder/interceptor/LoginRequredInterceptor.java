package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by nowcoder on 2016/7/17.
 */
@Component
// 第二个拦截器，拦截器有优先级的概念,第一个拦截器是passportInterceptor
public class LoginRequredInterceptor implements HandlerInterceptor {
    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 如果用户没登录，直接跳转到登录/注册界面加上callBack（这里的callBack是next）字段
        if (hostHolder.getUser() == null) {
            // getRequestURL()：得到当前url的部分字段
            // 下一行就是直接跳转（并加上next字段）！！感觉是controller的功能。。。
            httpServletResponse.sendRedirect("/reglogin?next=" + httpServletRequest.getRequestURI());
        }
        return true;
    }
    // 然后将该拦截器在configuration中进行注册，下面在configuration继续讲解

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
    // 之后实现登录页面的跳转，在LoginController里面继续实现
}
