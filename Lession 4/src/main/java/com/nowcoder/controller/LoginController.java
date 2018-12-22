package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);//常量

    @Autowired
    UserService userService;
    
    //在/reglogin界面点击注册后跳转到/reg界面；注册一般是写入的,所以用POST
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.POST})
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("next") String next,
                      @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                      HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }
                //如果注册没错误，跳回主页？？？？（yes）
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";//注册完成也跳回登录页面
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            model.addAttribute("msg", "服务器错误");//如果注册有错误，则应该把错误返回给模板（前端）
            return "login";//注册异常，返回登录页面
        }
    }

    //登录注册页面（yes）
    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String regloginPage(Model model, @RequestParam(value = "next", required = false) String next) {
        // 将next字段添加到velocity上下文中，而在login.html 42行：next变量被埋下 ：<input type="hidden" name="next" value="$!{next}"/>
        model.addAttribute("next", next);
        return "login";
    }

    //登陆密码输入验证与响应，token（login_ticket）与服务器的关联,每一次成功的登陆数据库都会记录一条login_ticket表的信息，status = 1,登出时（或者设置记住我后）status = 0
    //表示该表的该条信息已经无效（cookie无效）
    //跳到登录界面（在reglogin页面注入账号密码并点击登陆后跳转到该页面？？？？？？？？？？？:yes，也用POST）
    @RequestMapping(path = {"/login/"}, method = {RequestMethod.POST})
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        // 解析跳转地址，required=false表示该参数可传可不传,在注册登录界面/reglogin 61行中首先要解析next字段
                        @RequestParam(value="next", required = false) String next,
                        @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,//页面的记住我功能
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);//用户登录服务:验证账号密码，实现账户登录与ticket字段的建立
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                // 给浏览器下发一个包含ticket的cookie
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                // 如果url中next字段不为空，则跳转到url为next的界面，否则跳回主页
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";//登录成功则跳回主页
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            logger.error("登陆异常" + e.getMessage());
            return "login";//返回的这个模板实际上是登录注册界面/reglogin
        }
    }

    // 用户登出
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    // 从用户请求中的cookie中将将数据库中的login_ticket"删掉"（这里是改变login_ticket的status为1 即为无效）
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

    // 拓展：seession ID的应用：登录阿里旺旺之后不用再登录淘宝登录页面，服务器共用 一个SessionId，在请求网页版的时候自动将阿里旺旺的SessionId传入
    // 下面讲未登录跳转，即请求某些页面url必须要登录，如果未登录则跳转到登录页面，登录之后就直接跳回之前的页面
    // 实现方式：在跳转到登录/注册界面的额时候url中加上参数callBack（/login?callback=xxxxxx）;再做一个拦截器LoginRequredInterceptor
}
