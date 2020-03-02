package com.ch.community.controller;

import com.ch.community.dto.AccessTokenDTO;
import com.ch.community.dto.GithubUser;
import com.ch.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.url}")
    private String redirectUrl;
    @GetMapping("/callback")
    public String callback(String code,String state){
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
            return "redirect:index";
        }else{
            return "redirect:index";
        }

    }
}
