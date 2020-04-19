package com.ch.community.tool;

import com.ch.community.mapper.NotificationMapper;
import com.ch.community.mapper.UserMapper;
import com.ch.community.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class GeneralTools {



    @Autowired
    private UserMapper userMapper;



    public User getUserByToken(String token){
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(token);
        return userMapper.selectByExample(userExample).get(0);
    }


}