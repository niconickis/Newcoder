package com.nowcoder.controller;

import com.nowcoder.aspect.LogAspect;
import com.nowcoder.model.User;
import com.nowcoder.service.WendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by nowcoder on 2016/7/10.
 */
@Controller//第一部分：表示这个对象是个controller
public class IndexController { //该类表示的首页的controller
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    WendaService wendaService;  

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})//表示首页的路径
    //@ResponseBody//表示的是返回给用户的的直接是个字符串，而不是templates中的模板；如果返回的是个模板，则需要将ResponseBody注释掉然后返回的对象会在template中寻找
    public String index(HttpSession httpSession) {
        logger.info("VISIT HOME");
        return "index";
        // return wendaService.getMessage(2) + "Hello NowCoder" + httpSession.getAttribute("msg");//访问RequestMapping中的地址，就返回该字符串
    }

    //url中參數的解析
    @RequestMapping(path = {"/profile/{groupId}/{userId}"})//比如解析url中的用戶變量
    @ResponseBody
    public String profile(@PathVariable("userId") int userId, //路徑裡的參數解析
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "2") int type,//url中請求參數的解析，例如type和key:http://127.0.0.1:8080/profile/zhangxuyan/123?type=456&key=myself
                          @RequestParam(value = "key", defaultValue = "Are you ok?") String key) {//如果不提供key或者value的話。則會出現error,所以可以設定默認值，而參數required默認為true
        return String.format("Profile Page of %s / %d, t:%d k: %s", groupId, userId, type, key);
    }

    //第二部分：HTTP Method:Get,Post,Head,Put,Delete,Opitions，講了一堆，沒聽

    //第三部分：模板的概念，就是返回的不能寫一堆html的字符串，可以引用template中寫好的html,所以這裡不需要用@ResponseBody
    @RequestMapping(path = {"/vm"}, method = {RequestMethod.GET})
    public String template(Model model) {//接口Model,給模板傳遞後台變量的類
        model.addAttribute("value1", "vvvvv1");//給模板中添加一個叫value1的變量，值為vvvvv1,在template的文件中引用該變量時：$!{value1},模板中的！表示為如果改變量在模板中不存在，則為空，如果模板中不加！則將該變量當成字符串解析
        List<String> colors = Arrays.asList(new String[]{"RED", "GREEN", "BLUE"});
        model.addAttribute("colors", colors);//給模板版传递一个复杂变量：字符串數組變量
        model.addAttribute("saohua", "nihaosaoa");
        //思考：如果想要打印colors中的每一個元素怎麼辦？1.在controller中用循環打印（但需要@ResponseBody）2.在模板中用velocity寫循環，如下三行：
        // #foreach($color in $colors)
        // This is Color $!{foreach.index}: $color, $!{foreach.count}
        // #end
        //在templates中變量都用$開頭

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; ++i) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        //velocity 中的变量方法都可以用相应java语言加$号实现
        model.addAttribute("map", map);

        //除了加变量之外，还可以在模板中加入自定义的对象，对象现在model中定义，然后还是用Model.addAttribute()方法传递给模板，不过第二个参数要初始化
        //而在templates中，自定义对象的方法的调用直接调用方法名而不用加括号，如下：$!{user.name} 调用了名为user的对象的方法getName,不需要加括号,方法的定义要遵循命名规则！！！！
        model.addAttribute("user", new User("LEE"));
        return "home";//home.vm在templates中定義，返回的時候不加後綴也可以識別，文件後綴變成html是在templates中的application.properties中定義的：spring.velocity.suffix=.html
    }

        //模板中还可以自己定义变量：用#set()语句定义，例如：
        // #set($title = "nowcoder_title")
        // Title: $!{title}

        //模板对模板的继承：比如在模板中写了header.html这个文件，则可以在其他模板中直接用 ：#parse("header.html")或者：#include("header.html")来进行对header模板的继承，注意用
        //parse语句会对header.html中的变量作解析，而include不会，include会以字符串的形式对变量进行解析，parse和include主要就用在头尾上

        //模板的宏： #macro():作用：每个页面公共的部分，提高复用性：例如
        // #macro (render_color, $index, $color)
        //     Color Render Macro $index, $color
        // #end
        //上面的部分为宏定义，render_color为该宏的名称，index和color为参数，下面的部分为宏的调用，直接给render_color这个宏传递两个参数即可调用该宏。看起来相当于velocity的“方法”
        // #foreach($color in $colors)
        //     #render_color($foreach.index, $color)
        // #end

    //第四部分：request与response
    //request:请求的是什么；功能：参数解析、cookie读取、http请求字段、文件上传；重要的方法：getHeaderNames()、getMethod()、getPathInfo()、getQueryString
    //response：返回的是什么；功能：页面内容返回、cookie下发、http字段设置，headers里的内容添加；重要的方法：addCookie(new Cookie(key,value))、addHeader(key,value)
    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})//定义了一个请求的url
    @ResponseBody //返回的是字符串（文本）不是模板
    public String request(Model model, HttpServletResponse response, //response:返回给用户的东西
                           HttpServletRequest request,
                           HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId) {
        StringBuilder sb = new StringBuilder();//String下的一个子类
        sb.append("COOKIEVALUE:" + sessionId);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                sb.append("Cookie:" + cookie.getName() + " value:" + cookie.getValue());
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        response.addHeader("nowcoderId", "hello");//返回给用户的Response Headers中包含名为nowcoderId,值为hello的东西
        response.addCookie(new Cookie("username", "nowcoder"));//返回给用户的cookie:名为username,值为nowcoder

        return sb.toString();//返回用户请求的一些信息
    }

    //第五部分之一：重定向
    //301跳转与302跳转，301：永久跳转；302：临时跳转
    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,  //返回的是另一个网页（RedirectView）而不是一个String了
                                 HttpSession httpSession) {
        httpSession.setAttribute("msg", "jump from redirect");//把消息带到跳转的那个页面去，名字为msg,值为“jump from redirect”
        //然后在转到主页的public String index方法中加入参数 HttpSession httpSession，返回的语句变为如下：
        //return "Hello NowCoder" + httpSession.getAttribute("msg")

        RedirectView red = new RedirectView("/", true);//指定跳转的相对路径，后面true表示为相对路径，否则默认为false
        if (code == 301) {  //强制的301跳转,感觉没什么用啊，跳转最后都是到主页上来？？？还是用red自定义？没搞懂
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return  red;
    }
    //跳转的用处：1.通过用户的request请求解析user agent,如果是手机用户则跳转到手机页面；2.业务分发改变网站地址，及时分发给了用户不用的url也可以针对该url跳转

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) { //我先在知道为啥这么定义一个方法了，每一个方法都对应一个页面（url）,所以都要有一个return给用户
        if ("admin".equals(key)) {    //http://127.0.0.1:8080/admin?key=admin
            return "hello admin";
        }
        //http://127.0.0.1:8080/admin?key=asdh
        throw  new IllegalArgumentException("参数不对");//抛出异常后在@ExceptionHandler()处统一处理
    }

    //第五部分之二：异常的捕获与处理
    // @RequestMapping(path={"/error"},method={RequestMethod.GET})
    // public String errorUrl() {
    //     return "404 Not Found" ;
    // }

    // @ExceptionHandler()
    // public RedirectView redirect(Exception e) {
    //     RedirectView red = new RedirectView("/error",true);
    //     return red;
    // }

    //在@ExceptionHandler()处统一处理抛出的异常
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
        //在这里可以做一个改进：如果url不存在，则统一抛出一个页面。e.g:404
    }

    //第六部分：IoC:依赖注入：在service中定义 e.g：在每个controller中都需要service中的WendaService
    //那么每个都controller文件中都需要一个初始化： WendaService wendaService = new WendaService()
    //但是现在只要给service中的WendaService 加上前缀@service, 则在任何一个controller中都只要用前缀@Autowired WendaService wendaService; 即可，不需要在初始化
    //好处：不需要再关心变量初始化，只需用注解的方式

    
    //第六部分：AOP:面向切面编程(处理所有的服务e.g：比如所有的controller中的参数都要记录，以方便以后对请求的统计及性能分析)，在aspect中写；下面先看aspect package中的LogAspect.java 会在那里继续写
}
