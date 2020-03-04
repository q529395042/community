package com.ch.community.controller;

import com.ch.community.dto.AccessTokenDTO;
import com.ch.community.dto.GithubUser;
import com.ch.community.mapper.UserMapper;
import com.ch.community.module.User;
import com.ch.community.provider.GithubProvider;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.url}")
    private String redirectUrl;
    @GetMapping("/callback")
    public String callback(String code, String state, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        System.out.println(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUrl);
        String acessToken = githubProvider.getAcessToken(accessTokenDTO);
        System.out.println(acessToken);
        GithubUser user = githubProvider.getUser(acessToken);
        if(user!=null){
            httpServletRequest.getSession().setAttribute("user",user);
            User user1=new User();
            user1.setAccountId(user.getId().toString());
            user1.setName(user.getName());
            user1.setToken(UUID.randomUUID().toString());
            user1.setGmtCreate(System.currentTimeMillis());
            user1.setGmtModified(System.currentTimeMillis());
            userMapper.insertUser(user1);
            httpServletResponse.addCookie(new Cookie("token",user1.getToken()));
            return "redirect:/";
        }else{
            return "redirect:/";
        }

    }
}
