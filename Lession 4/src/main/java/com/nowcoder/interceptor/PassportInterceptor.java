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
 * Created by nowcoder on 2016/7/3.
 */
// 本拦截器功能：识别用户，用户的身份验证
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    @Override
    // 取出数据库中的ticket并与客户端的比较
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        // 遍历用户的请求的Cookie
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null) {
            // 拦截器也可以调用dao层
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            // 如果请求中的ticket在数据库中不存在或者ticket已过期或者ticket有效状态不为0，则表示用户没有登录，所以返回True在Controller继续作相应的处理；如果返回false就不继续Controller层的处理了
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }
            // 如果ticket“有效”,则应该ticket关联的User信息取出放到上下文，则后面的链路都可取到这个上下文
            User user = userDAO.selectById(loginTicket.getUserId());
            // 怎么存上面取出的User以保证后面的链路都能得到呢？：大家都能得到：Ioc的思想。在model中定义了一个HostHolder,将取出的用户放进去，下面看HostHolder文件
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    // ModelAndView表示model层和static和模板层，加入这个参数就可以在模板中访问这个modelandview.addobject加入的变量了；这实际上与controller层的model.addattribute作用类似
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        // 如果模板不为空，且HosyHolder持有用户，则提供给模板调用该user属性的的“权限“：即将用户User放到用velocity写的渲染中去
        if (modelAndView != null && hostHolder.getUser() != null) {
            // 将用户信息放到模板中
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        // 请求及处理结束后释放User信息
        hostHolder.clear();
    }
}
// 下面就要把写好的这个Interceptor配置到web service里：即在com.nowcoder.configuration里写一个webService的配置文件,转到configuration里面去
