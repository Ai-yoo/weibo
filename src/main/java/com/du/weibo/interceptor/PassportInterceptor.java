package com.du.weibo.interceptor;

import com.du.weibo.dao.LoginTicketDao;
import com.du.weibo.dao.UserDao;
import com.du.weibo.model.HostHolder;
import com.du.weibo.model.LoginTicket;
import com.du.weibo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created with IDEA
 *
 * @author duzhentong
 * @Date 2018/8/29
 * @Time 9:38
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    LoginTicketDao loginTicketDAO;

    @Autowired
    UserDao userDAO;

    @Autowired
    HostHolder hostHolder;

    /**
     * 请求开始之前调用，如果返回false后面的的拦截不再生效
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if ("ticket".equals(cookie.getName())) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUsesr(user);

        }
        return true;
    }

    /**
     * 请求处理完后渲染之前
     * 这样在服务器处理之后，用户的信息就存在ThreadLocal中，在渲染之前就可以把用户信息
     * 利用AOP传到所有页面
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    /**
     * 全部渲染后，再次回调，主要用于清除数据
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
