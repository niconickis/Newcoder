package com.nowcoder.interceptor;

import com.nowcoder.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by nowcoder on 2016/7/3.
 */
// 拦截器的概念：http请求————>controller处理—————>模板的渲染。拦截器主要作用在controller前后，整个链路上留下回调接口：链路回调的思想
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    // preHandle：在controller之前，需要走的拦截器：主要作用是http请求有没有权限往下走
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (hostHolder.getUser() == null) {
            httpServletResponse.sendRedirect("/reglogin?next=" + httpServletRequest.getRequestURI());
            return false;
        }
        return true;
    }

    @Override
    // controller之后与渲染（返回模板）之前需要调用的方法：主要作用是渲染之前一些数据是否要推给渲染（给模板）
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    // 整个请求完成后要调用的：主要作用是把之前的一些处理都删掉
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
