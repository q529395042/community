package com.ch.community.controller;

import com.ch.community.dto.CommentCreateDTO;
import com.ch.community.dto.CommentDTO;
import com.ch.community.dto.QuestionDTO;
import com.ch.community.dto.ResultDTO;
import com.ch.community.enums.CommentTypeEnum;
import com.ch.community.enums.NotificationStatusEnum;
import com.ch.community.exception.CustomizeErrorCode;
import com.ch.community.exception.CustomizeException;
import com.ch.community.mapper.NotificationMapper;
import com.ch.community.mapper.QuestionMapper;
import com.ch.community.mapper.UserMapper;
import com.ch.community.model.*;
import com.ch.community.service.CommentService;
import com.ch.community.service.QuestionService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationMapper notificationMapper;
    
    @GetMapping("/question/{id}")
    public String Question(@PathVariable("id") Integer id, @RequestParam(name = "notifyId",required = false)Integer notifyId, Model model, HttpServletRequest request){

        if (notifyId!=null){
            Notification notification=new Notification();
            notification.setId(notifyId);
            notification.setStatus(NotificationStatusEnum.READ.getStatus());
            notificationMapper.updateByPrimaryKeySelective(notification);
        }
        QuestionDTO question = questionService.getQuestionById(id);
        List<QuestionDTO> relateds=questionService.selectRelated(question);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NO_FOUND);
        }
        model.addAttribute("question",question);
        Question question1=new Question();
        question1.setViewCount(question.getViewCount()+1);
        QuestionExample questionExample=new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(id);
        questionMapper.updateByExampleSelective(question1,questionExample);
        ResultDTO commentDTOS=commentService.selectCommentList(id, CommentTypeEnum.Question);
        model.addAttribute("comments",commentDTOS.getData());
        model.addAttribute("relateds",relateds);
        return "question";
    }


    @GetMapping("/hot")
    public String hot(HttpServletRequest request, Model model, @RequestParam(name = "pageCount",defaultValue = "5",required = false) Integer pageCount, @RequestParam(name = "currentPage",defaultValue = "0",required = false) Integer currentPage, @RequestParam(name = "pageSum",defaultValue = "0",required = false) Integer pageSum){
        QuestionExample questionExample=new QuestionExample();

        if (currentPage<1){
            currentPage=0;
        }
        if (pageSum==0) {

            pageSum = (questionMapper.selectByExample(questionExample).size() % pageCount == 0 ? questionMapper.selectByExample(questionExample).size() / pageCount :questionMapper.selectByExample(questionExample).size() / pageCount + 1);
        }
        if (currentPage>pageSum){
            currentPage=pageSum;
        }
        questionExample.setOrderByClause("view_count desc");
        RowBounds rowBounds=new RowBounds(currentPage*pageCount,pageCount);
        List<Question> questions=questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample,rowBounds);
        List<QuestionDTO> questionDTOS =new ArrayList<>();
        for (Question question : questions) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        model.addAttribute("hotQuestion",questionDTOS);
        model.addAttribute("pageCount",currentPage);
        model.addAttribute("pageSum",pageSum);
        model.addAttribute("currentPage",currentPage);
        return "hot";
    }

}
