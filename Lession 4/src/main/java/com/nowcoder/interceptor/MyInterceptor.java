package com.nowcoder.interceptor;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.HostHolder;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MyInterceptor implements HandlerInterceptor{

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserDAO userDAO;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 加一段Cookie
        Cookie cookie = new Cookie("try_cookie","375923785230".toString());
        cookie.setComment("This is my first created cookie for client");
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 10);
        httpServletResponse.addCookie(cookie);
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest httpServletRequest , HttpServletResponse httpServletResponse,Object o,ModelAndView modelAndView) throws Exception{
        // 取出上一个cookie，然后啥也不干
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie:cookies){
            if (cookie.getName() == "try_cookie"){
                String wantedValue = cookie.getValue();
            }
        }
  
        
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,Object o,Exception e) throws Exception{

    }
    // 然后在configuration中加入我的拦截器，注意顺序
}

