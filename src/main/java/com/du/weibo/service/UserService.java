package com.du.weibo.service;

import com.du.weibo.aspect.LogAspect;
import com.du.weibo.dao.LoginTicketDao;
import com.du.weibo.dao.UserDao;
import com.du.weibo.model.LoginTicket;
import com.du.weibo.model.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author duzhentong
 * @Date 2018/8/29
 * @Time 9:44
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private UserDao userDAO;

    @Autowired
    private LoginTicketDao loginTicketDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    /**
     * 为什么返回值使用map，因为，用户可能登录成功，失败，用户名密码错误等等情况，使用map
     * 存储，如果登录成功，直接返回一个空的map即可
     * @param username
     * @param password
     * @return
     */
    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<String,String>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        System.out.println("调用selectByName之前");
        User user = userDAO.selectByName(username);
        if (user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setPassword(password);
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);

        return map;
    }

    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<String,String>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        System.out.println(user.toString());
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!password.equals(user.getPassword())) {
            map.put("msg", "密码错误");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        map.put("userId", String.valueOf(user.getId()));
        return map;
    }

    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600 * 24 * 100 + now.getTime());
        System.out.println("时间上限："+now);
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }

    public User selectBuName(String name) {
        return userDAO.selectByName(name);
    }
}
