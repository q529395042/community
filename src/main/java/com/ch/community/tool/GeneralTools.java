package com.ch.community.tool;

import com.ch.community.enums.NotificationStatusEnum;
import com.ch.community.mapper.MemberMapper;
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
    private MemberMapper memberMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    public Integer isUserOrMember(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        httpServletRequest.getSession();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    MemberExample memberExample = new MemberExample();
                    memberExample.createCriteria().andTokenEqualTo(token);
                    List<Member> memberList = memberMapper.selectByExample(memberExample);
                    if (memberList.size() != 0) {
                        return 0;
                    } else {
                        UserExample userExample = new UserExample();
                        userExample.createCriteria().andTokenEqualTo(token);
                        List<User> users = userMapper.selectByExample(userExample);
                        if (users.size() != 0) {
                            return 1;
                        }
                    }

                }
            }
        }
        return 2;
    }

    public User getUserByToken(String token){
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(token);
        return userMapper.selectByExample(userExample).get(0);
    }

    public Member getMemberByToken(String token){
        MemberExample memberExample=new MemberExample();
        memberExample.createCriteria().andTokenEqualTo(token);
        return memberMapper.selectByExample(memberExample).get(0);
    }

}