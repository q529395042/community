package com.ch.community.service;

import com.ch.community.dto.PagesDTO;
import com.ch.community.dto.QuestionDTO;
import com.ch.community.mapper.QuestionExtMapper;
import com.ch.community.mapper.QuestionMapper;
import com.ch.community.mapper.UserMapper;
import com.ch.community.model.QuestionExample;
import com.ch.community.model.Question;
import com.ch.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;



    public List<QuestionDTO> getQuestionList(Integer pageCount,Integer currentPage) {
        PagesDTO pagesDTO = new PagesDTO();
        QuestionExample questionExample=new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        RowBounds rowBounds=new RowBounds(pageCount,currentPage);
        List<Question> questionList =questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample,rowBounds);
        List<QuestionDTO> questionDTOS =new ArrayList<>();
        for (Question question : questionList) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        return questionDTOS;
    }


    public List<QuestionDTO> getProfileQuestionList(Integer id,Integer pageCount,Integer currentPage) {
        PagesDTO pagesDTO = new PagesDTO();
        QuestionExample questionExample=new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(id);

        RowBounds rowBounds=new RowBounds(pageCount,currentPage);
        List<Question> questionList =questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample,rowBounds);
        List<QuestionDTO> questionDTOS =new ArrayList<>();
        for (Question question : questionList) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        return questionDTOS;
    }

    public QuestionDTO getQuestionById(Integer id){
        Question question=questionMapper.selectByPrimaryKey(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user=userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }


    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String regexpTag=questionDTO.getTag().replace(",","|");
        Question question=new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions=questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS=questions.stream().map(q->{
            QuestionDTO questionDTO1=new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());
        return questionDTOS;
    }

    public List<QuestionDTO> selectBySearch(String search) {
        String selectBysearch=search.replace(" ","|");
        List<Question> questions=questionExtMapper.selectBySearch(search);
        List<QuestionDTO> questionDTOS=new ArrayList<>();
        for (Question question : questions) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        return questionDTOS;
    }
}
