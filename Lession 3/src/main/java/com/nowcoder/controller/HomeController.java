package com.nowcoder.controller;

import com.nowcoder.model.Question;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
// 底层mybatis和mysql参数和版本的问题，由于安装的是mysql是8.0.13版本,所以在pom.xml中配置文件mysql的mysql-connector-java包时要加上版本号（之前都没加上，导致mysql的connnector与mybatism没有
// 映射？？？），加上之后重新debug WendaAppplication.java时就会自动更新connector,就会与mybatis和mysql版本匹配，高级版本的mysql的jdbc的url参数中还必须加上serverTimezone这个参数，然后就测试的
// debug通过了

import javax.servlet.http.HttpSession;

/**
 * Created by nowcoder on 2016/7/15.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    //contrller层调用dao层，把数据库中的数据取出。总结一下：dao,service以及controller层的IOC的应用都是从下层往上层的
    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit); //得到所有的question信息
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) { //从questionList里取出每个question数据，然后将放到question放到ViewObject里
            ViewObject vo = new ViewObject();
            vo.set("question", question);//将整个question数据放入以“question”为键放到vo中
            vo.set("user", userService.getUser(question.getUserId()));
            // 以“user”为键通过question model中的方法（question.getUserId()）获取用户的ID信息，然后以ID信息用过service层的
            // getUser方法获取相关ID的用户信息然后加入vo中
            vos.add(vo);//将小vo加入到大vos中,然后你就可以在模板中使用归好类的vos对象了。
            // Q:如果在模板中直接写questionList的递归是不是也可以？行！！！！因为你是要递归取得每一个question的数据的而不能准确定位，所以你只能在循环语句中对每一段模板进行操作，中间不能加入任何
            // 额外的东西，而又hashmap则可以任意获取
        }
        return vos;
    }

    //主页的响应
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", getQuestions(0, 0, 10));//getQuestions是从哪里来的？？？service or controller?？？？？？？？在本文件的HomeController里面定义了该方法
        return "index";
    }

    //用户ID点击的响应.比如点击某个人的userID后进入本人的界面
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        return "index";
    }

    //这玩意没在工作啊，还是LESSION 2 的时候的相应啊，那怎么把lession 2 unable 呢？？
    @RequestMapping(path = {"/message"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String UserMessageIndex(HttpSession httpSession) {
        return "HomeContrller is working";
    }
    //54和61行的getQuestions()中的参数userID一个为零一个不为零
}
