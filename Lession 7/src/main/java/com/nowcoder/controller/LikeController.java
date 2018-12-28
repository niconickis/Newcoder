package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.LikeService;
import com.nowcoder.util.WendaUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController{
    @Autowired
    LikeService likeService;
    
    @Autowired
    HostHolder hostHolder;

    // 用户点赞了的请求：/like
    @RequestMapping(path = {"/like"},method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        System.out.println("Is reponing like button");
        if (hostHolder.getUser() == null){
            // 表示用户未登录
            return WendaUtil.getJSONString(999);
        }
        // **********用参数entityType(在EntityTyp这个model中定义，这里是ENTITY_COMMENT)和entityId进行读取,这里的commentId就是entityId**********
        long likeCount = likeService.like(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"},method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId){
        System.out.println("Is reponing dislike button");
        if (hostHolder.getUser() == null){
            // 表示用户未登录
            return WendaUtil.getJSONString(999);
        }
        // **********用参数entityType(在EntityTyp这个model中定义，这里是ENTITY_COMMENT)和entityId进行读取,这里的commentId就是entityId**********
        // 这里改成dislike方法
        long likeCount = likeService.disLike(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
        // 下面在questionController里将赞踩的信息显示出来
    }
}