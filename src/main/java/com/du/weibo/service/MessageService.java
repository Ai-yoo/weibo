package com.du.weibo.service;

import com.du.weibo.dao.MessageDao;
import com.du.weibo.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author duzhentong
 * @Date 2018/8/29
 * @Time 10:35
 */
@Service
public class MessageService {

    @Autowired
    MessageDao messageDao;

    /**
     * 增加评论方法
     * @param message
     * @return
     */
    public int addMessage(Message message) {
        return messageDao.addMessage(message) > 0 ? message.getId() : 0;
    }

    /**
     * 获取评论列表
     * @param offset 起始位置
     * @param limit 偏移量
     * @return
     */
    public List<Message> getMessageList(int offset, int limit) {
        return messageDao.getMessageList(offset, limit);
    }


}
