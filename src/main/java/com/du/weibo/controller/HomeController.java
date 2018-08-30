package com.du.weibo.controller;

import com.du.weibo.model.*;
import com.du.weibo.service.FollowService;
import com.du.weibo.service.MessageService;
import com.du.weibo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author duzhentong
 * @Date 2018/8/29
 * @Time 10:42
 */
@Controller
public class HomeController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/", "/home"}, method = {RequestMethod.GET})
    public String index(Model model) {
        model.addAttribute("vos", getMessage(0, 0, 10));
        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getMessage(userId, 0, 10));

        User user = userService.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }

    private List<ViewObject> getMessage(int userId, int offset, int limit) {
        List<Message> messageList = messageService.getMessageList(offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Message message : messageList) {
            ViewObject vo = new ViewObject();
            vo.set("message", message);
            vo.set("user", userService.getUser(message.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
