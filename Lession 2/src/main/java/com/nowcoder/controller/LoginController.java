package com.nowcoder.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController{
    @RequestMapping(path = {"/login"},method = RequestMethod.GET)
    public String login(HttpSession httpSession) {
        return "login";
    }
}