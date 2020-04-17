package com.ch.community.controller;

import com.ch.community.dto.CommentCreateDTO;
import com.ch.community.dto.ResultDTO;
import com.ch.community.enums.CommentTypeEnum;
import com.ch.community.exception.CustomizeErrorCode;
import com.ch.community.exception.CustomizeException;
import com.ch.community.model.Comment;
import com.ch.community.model.Member;
import com.ch.community.model.User;
import com.ch.community.service.CommentService;
import com.ch.community.tool.GeneralTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private GeneralTools generalTools;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public ResultDTO post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        System.out.println(commentCreateDTO);
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        comment.setCommentator(user.getId().toString());
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setLikeCount(0);
        comment.setTargetId(commentCreateDTO.getTargetId());
        ResultDTO resultDTO=commentService.insert(comment,user);
        return resultDTO;
    }
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO getCommentList(@PathVariable(name = "id") Integer id){
        return commentService.selectCommentList(id,CommentTypeEnum.Comment);
    }
}
