package com.du.weibo.controller;

import com.du.weibo.aspect.LogAspect;
import com.du.weibo.model.HostHolder;
import com.du.weibo.model.Message;
import com.du.weibo.service.MessageService;
import com.du.weibo.util.WeiboUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author duzhentong
 * @Date 2018/8/29
 * @Time 17:26
 */

@Controller
public class MessageController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            Message message = new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            System.out.println("时间："+message.getCreatedDate());
            if (hostHolder.getUser() == null) {
                return WeiboUtil.getJSONString(999);
            } else {
                message.setUserId(hostHolder.getUser().getId());
            }
            int code=messageService.addMessage(message);
            if (code > 0) {
                return WeiboUtil.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("添加评论失败" + e.getMessage());
        }
        return WeiboUtil.getJSONString(1, "失败");
    }
}
