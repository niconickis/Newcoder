package com.nowcoder.service;

import java.util.List;

import com.nowcoder.dao.MessageDao;
import com.nowcoder.model.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class MessageService{
    @Autowired
    MessageDao messageDao;

    public List<Message> getLatestMessages(int fromid,int offset,int limit) {
        return messageDao.selectLatestMessages(fromid,offset,limit);
    }
}