package com.du.weibo.controller;

import com.du.weibo.model.EntityType;
import com.du.weibo.model.HostHolder;
import com.du.weibo.model.User;
import com.du.weibo.model.ViewObject;
import com.du.weibo.service.FollowService;
import com.du.weibo.service.MessageService;
import com.du.weibo.service.UserService;
import com.du.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class FollowController {
    @Autowired
    FollowService followService;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return WeiboUtil.getJSONString(999);
        }

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);

        // 返回关注的人数
        return WeiboUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return WeiboUtil.getJSONString(999);
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        // 返回关注的人数
        return WeiboUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);

        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            System.out.println(getUsersInfo(hostHolder.getUser().getId(), followerIds).toString());
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            System.out.println("getuserinfo方法"+user.toString());
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, uid));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
