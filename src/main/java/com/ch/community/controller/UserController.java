package com.ch.community.controller;

import com.ch.community.dto.ResultDTO;
import com.ch.community.exception.CustomizeErrorCode;
import com.ch.community.mapper.UserMapper;
import com.ch.community.model.User;
import com.ch.community.model.UserExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultDTO userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response){
        UserExample userExample=new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(password);
        List<User> userList=userMapper.selectByExample(userExample);
        if (userList.size()==0){
            return  ResultDTO.errorOf(CustomizeErrorCode.USERNAME_ERROR);
        }else{
            response.addCookie(new Cookie("token",userList.get(0).getToken()));
            request.getSession().setAttribute("user",userList.get(0));
        }
        return  ResultDTO.success();
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResultDTO registerMember(String username, String password,String name,HttpServletRequest request,HttpServletResponse response){
        User user=new User();
        if (StringUtils.isBlank(username)||StringUtils.isBlank(password)||StringUtils.isBlank(name)){
            return  ResultDTO.errorOf(CustomizeErrorCode.NOT_USERNAME);
        }
        UserExample userExample=new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> userList=userMapper.selectByExample(userExample);
        if (userList.size()!=0){
            return  ResultDTO.errorOf(CustomizeErrorCode.USERNAME_EXIST);
        }
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setToken(UUID.randomUUID().toString());
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModify(System.currentTimeMillis());
        user.setAvatarUrl("/images/hide-name.png");
        userMapper.insertSelective(user);
        response.addCookie(new Cookie("token",user.getToken()));
        request.getSession().setAttribute("user",user);
        return ResultDTO.success();
    }

}
