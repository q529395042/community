package com.ch.community.controller;

import com.ch.community.mapper.UserMapper;
import com.ch.community.module.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;
    
    @GetMapping("/")
    public String hello(HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    User user=userMapper.findByToken();
                    if (user!=null){
                        httpServletRequest.getSession().setAttribute("user",user);
                        return "index";
                    }
                    break;
                }
            }
        }
        return "index";
    }
}
