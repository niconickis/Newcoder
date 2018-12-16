package com.nowcoder.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LetterController{
    @RequestMapping(path = {"/letter"},method = {RequestMethod.GET})
    // @ResponseBody
    public String Detail(HttpSession httpSession){
        return "letter";
    }
}