package com.ch.community.controller;

import com.ch.community.dto.AccessTokenDTO;
import com.ch.community.dto.GithubUser;
import com.ch.community.mapper.UserMapper;
import com.ch.community.model.User;
import com.ch.community.model.UserExample;
import com.ch.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUrl);
        String acessToken = githubProvider.getAcessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(acessToken);
        User user1=new User();
        if (user!=null) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(user.getId().toString());
            List<User> users = userMapper.selectByExample(userExample);
            if (userMapper.selectByExample(userExample)!=null){
                httpServletRequest.getSession().setAttribute("user", user);
                httpServletResponse.addCookie(new Cookie("token", users.get(0).getToken()));
                return "redirect:/";
            }
        }
        if(user==null){
            user1.setAccountId(user.getId().toString());
            user1.setName(user.getName());
            user1.setToken(UUID.randomUUID().toString());
            user1.setGmtCreate(System.currentTimeMillis());
            user1.setGmtModify(System.currentTimeMillis());
            user1.setAvatarUrl(user.getAvatar_url());
            userMapper.insert(user1);
            httpServletRequest.getSession().setAttribute("user",user);
            httpServletResponse.addCookie(new Cookie("token",user1.getToken()));
            return "redirect:/";
        }else{
            return "redirect:/";
        }

    }
}
