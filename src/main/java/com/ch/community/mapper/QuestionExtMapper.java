package com.ch.community.mapper;

import com.ch.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    List<Question> selectRelated(Question question);

    List<Question> selectBySearch(String search);
}
