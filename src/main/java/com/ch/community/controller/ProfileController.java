package com.ch.community.controller;

import com.ch.community.dto.QuestionDTO;
import com.ch.community.enums.NotificationStatusEnum;
import com.ch.community.mapper.NotificationMapper;
import com.ch.community.mapper.QuestionMapper;
import com.ch.community.mapper.UserMapper;
import com.ch.community.model.*;
import com.ch.community.service.NotificationService;
import com.ch.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationMapper notificationMapper;

    @GetMapping("/profile/{action}")
    private String profile(@PathVariable(name = "action")String action,HttpServletRequest request, Model model, @RequestParam(name = "pageCount",defaultValue = "5",required = false) Integer pageCount, @RequestParam(name = "currentPage",defaultValue = "0",required = false) Integer currentPage, @RequestParam(name = "pageSum",defaultValue = "0",required = false) Integer pageSum){
       User sessionUser= (User) request.getSession().getAttribute("user");
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    UserExample userExample=new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users= userMapper.selectByExample(userExample);
                    if (users.size()!=0){
                        NotificationExample notificationExample=new NotificationExample();
                        notificationExample.createCriteria().andStatusEqualTo(NotificationStatusEnum.NO_READ.getStatus()).andReceiverEqualTo(sessionUser.getId());
                        List<Notification> noRead=notificationMapper.selectByExample(notificationExample);
                        model.addAttribute("noRead",noRead.size());
                        if ("questions".equals(action)) {
                            User user=users.get(0);
                                QuestionExample questionExample=new QuestionExample();
                                questionExample.createCriteria().andIdEqualTo(user.getId());
                                if (currentPage<1){
                                    currentPage=0;
                                }
                                if (pageSum==0) {

                                    pageSum = (questionMapper.selectByExample(questionExample).size() % pageCount == 0 ? questionMapper.selectByExample(questionExample).size() / pageCount :questionMapper.selectByExample(questionExample).size() / pageCount + 1);
                                }
                                if (currentPage>pageSum){
                                    currentPage=pageSum;
                                }
                            model.addAttribute("section", "questions");
                            model.addAttribute("sectionName", "我的提问");
                            List<QuestionDTO> questionDTOS=questionService.getProfileQuestionList(user.getId(),currentPage*pageCount,pageCount);
                            System.out.println(questionDTOS==null);
                            model.addAttribute("questions",questionDTOS);
                        }else if ("replies".equals(action)) {
                            User user=users.get(0);

                            notificationExample.createCriteria().andReceiverEqualTo(user.getId());
                            if (currentPage<1){
                                currentPage=0;
                            }
                            if (pageSum==0) {
                                pageSum=(notificationMapper.selectByExample(notificationExample).size() % pageCount == 0 ? notificationMapper.selectByExample(notificationExample).size() / pageCount :notificationMapper.selectByExample(notificationExample).size() / pageCount + 1);
                            }
                            if (currentPage>pageSum){
                                currentPage=pageSum;
                            }
                            List<Notification> notifications=notificationService.selectNoRead(user.getId(),currentPage*pageCount,pageCount);
                            model.addAttribute("notifications",notifications);
                            model.addAttribute("section", "replies");
                            model.addAttribute("sectionName", "最新回复");

                        }
                        model.addAttribute("pageCount",currentPage);
                        model.addAttribute("pageSum",pageSum);
                        model.addAttribute("currentPage",currentPage);

                        return "profile";
                    }
                }
                }
//            return "redirect:frist";
        }
//        List<QuestionDTO> questionDTOS=questionService.getQuestionList();
//        System.out.println(questionDTOS==null);
//        for (QuestionDTO questionDTO : questionDTOS) {
//            System.out.println(questionDTO.getCreator());
//        }
//        model.addAttribute("questions",questionDTOS);
        return "profile";
    }
}
