package com.nowcoder.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DetailController{
    @RequestMapping(path = {"/detail"},method = {RequestMethod.GET})
    // @ResponseBody
    public String Detail(HttpSession httpSession){ //我终于知道为啥httpsession的作用了：让一个用户的多次request得到，没有的话只能响应一次，第二次的话就没有定义了
        return "detail";
    }
}
