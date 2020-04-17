package com.ch.community.interceptor;

import com.ch.community.dto.ResultDTO;
import com.ch.community.enums.NotificationStatusEnum;
import com.ch.community.exception.CustomizeErrorCode;
import com.ch.community.mapper.MemberMapper;
import com.ch.community.mapper.NotificationMapper;
import com.ch.community.mapper.UserMapper;
import com.ch.community.model.*;
import com.ch.community.tool.GeneralTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = httpServletRequest.getCookies();
        httpServletRequest.getSession();
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    UserExample userExample=new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users=userMapper.selectByExample(userExample);
                    if (users.size()!=0){
                        httpServletRequest.getSession().setAttribute("user",users.get(0));
                        NotificationExample notificationExample=new NotificationExample();
                        notificationExample.createCriteria().andStatusEqualTo(NotificationStatusEnum.NO_READ.getStatus()).andReceiverEqualTo(users.get(0).getId());
                        List<Notification> noRead=notificationMapper.selectByExample(notificationExample);
                        httpServletRequest.getSession().setAttribute("noRead",noRead.size());
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
