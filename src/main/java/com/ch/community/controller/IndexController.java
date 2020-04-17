package com.ch.community.controller;

import com.ch.community.dto.QuestionDTO;
import com.ch.community.mapper.QuestionMapper;
import com.ch.community.mapper.UserMapper;
import com.ch.community.model.Question;
import com.ch.community.model.QuestionExample;
import com.ch.community.model.UserExample;
import com.ch.community.model.User;
import com.ch.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionMapper questionMapper;

    @GetMapping("/frist")
    public String index(Model model,@RequestParam(value = "search",required = false)String search, @RequestParam(name = "pageCount",defaultValue = "5",required = false) Integer pageCount,@RequestParam(name = "currentPage",defaultValue = "0",required = false) Integer currentPage,@RequestParam(name = "pageSum",defaultValue = "0",required = false) Integer pageSum){
        QuestionExample questionExample=new QuestionExample();
        List<Question> questions=new ArrayList<>();
        List<QuestionDTO> questionDTOS=new ArrayList<>();
        if (StringUtils.isNotBlank(search)){
            questionDTOS=questionService.selectBySearch(search);
        }else {
            questions=questionMapper.selectByExample(questionExample);
            questionDTOS=questionService.getQuestionList(currentPage*pageCount,pageCount);
        }
        if (currentPage<1){
            currentPage=0;
        }
        if (pageSum==0) {
            pageSum = ( questions.size()% pageCount == 0 ? questions.size() / pageCount : questions.size() / pageCount + 1);
        }
        if (currentPage>pageSum){
            currentPage=pageSum;
        }
//        List<QuestionDTO> questionDTOS=questionService.getQuestionList();
//        System.out.println(questionDTOS==null);
//        for (QuestionDTO questionDTO : questionDTOS) {
//            System.out.println(questionDTO.getCreator());

//        }
//        model.addAttribute("questions",questionDTOS);
        model.addAttribute("pageCount",currentPage);
        model.addAttribute("pageSum",pageSum);
        model.addAttribute("currentPage",currentPage);
        System.out.println(questionDTOS==null);
        model.addAttribute("search",search);
        model.addAttribute("questions",questionDTOS);
        return "frist";
    }
    
    @GetMapping("/")
    public String hello(HttpServletRequest httpServletRequest,Model model){
        Cookie[] cookies = httpServletRequest.getCookies();
        httpServletRequest.getSession();
        UserExample userExample=new UserExample();
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users=userMapper.selectByExample(userExample);
                    if (users.size()!=0){
                        User user=users.get(0);
                        httpServletRequest.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
//        List<QuestionDTO> questionDTOS=questionService.getQuestionList();
//        System.out.println(questionDTOS==null);
//        for (QuestionDTO questionDTO : questionDTOS) {
//            System.out.println(questionDTO.getCreator());
//        }
//        model.addAttribute("questions",questionDTOS);
        return "redirect:frist";
    }

    @GetMapping("/quit")
    public String quit(HttpServletRequest request, HttpServletResponse httpServletResponse){
        request.getSession().removeAttribute("user");
        Cookie cookie=new Cookie("token",null);
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
        return "redirect:frist";
    }


}
