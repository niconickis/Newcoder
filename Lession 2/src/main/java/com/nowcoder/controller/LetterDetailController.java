package com.nowcoder.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LetterDetailController{
    @RequestMapping(path = {"/letterDetail"},method = RequestMethod.GET)
    public String letterDetail(HttpSession httpSession) {
        return "letterDetail";
    }
}
