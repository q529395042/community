package com.ch.community.service;

import com.ch.community.dto.CommentCreateDTO;
import com.ch.community.dto.CommentDTO;
import com.ch.community.dto.ResultDTO;
import com.ch.community.enums.CommentTypeEnum;
import com.ch.community.enums.NotificationEnum;
import com.ch.community.enums.NotificationStatusEnum;
import com.ch.community.exception.CustomizeErrorCode;
import com.ch.community.exception.CustomizeException;
import com.ch.community.mapper.*;
import com.ch.community.model.*;
import com.ch.community.tool.GeneralTools;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private GeneralTools generalTools;

    @Transactional
    public ResultDTO insert(Comment comment,User user){
        if (comment.getParentId() == null ||comment.getParentId()==0) {
            return ResultDTO.errorOf(CustomizeErrorCode.Type_Error);
        }
        Comment comment1=commentMapper.selectByPrimaryKey(comment.getTargetId());
        if(comment.getType() ==null || !CommentTypeEnum.isExist(comment.getType())){
            if (comment1==null){
                throw new  CustomizeException(CustomizeErrorCode.Parent_Id_NOT_Found);
            }
        }

        String userName=user.getName();
        Question question=questionMapper.selectByPrimaryKey(comment.getParentId());
        if(comment.getType()==CommentTypeEnum.Comment.getType()){
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            if (comment1!=null){
                comment1.setGmtModified(System.currentTimeMillis());
                comment1.setCommentCount(comment1.getCommentCount()+1);
            }
            commentMapper.updateByPrimaryKeySelective(comment1);
            if (Integer.parseInt(comment.getCommentator())!=question.getCreator()){
                createNotify(comment,Integer.parseInt(comment.getCommentator()),NotificationEnum.COMMENT,userName,question.getTitle());
            }

        } else{
            //添加回复
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            if(comment1!=null)
            {
                comment1.setGmtModified(System.currentTimeMillis());
                comment1.setCommentCount(comment1.getCommentCount()+1);
                commentMapper.updateByPrimaryKeySelective(comment1);
            }
           question.setCommentCount(question.getCommentCount()+1);
            questionMapper.updateByPrimaryKeySelective(question);
            if (Integer.parseInt(comment.getCommentator())!=question.getCreator()){
                createNotify(comment,question.getCreator(),NotificationEnum.REPLY_QUESTION,userName,question.getTitle());
            }

        }
        return ResultDTO.success();
    }

    private void createNotify(Comment comment, Integer receiver, NotificationEnum notificationEnum, String natifierName, String outerTitle) {
        Notification notification=new Notification();
        notification.setNotifier(comment.getCommentator());
        notification.setNotifierName(natifierName);
        notification.setOuterTitle(outerTitle);
        notification.setOuterId(comment.getParentId());
        notification.setStatus(NotificationStatusEnum.NO_READ.getStatus());
        notification.setType(notificationEnum.getType());
        notification.setGmtCreate(comment.getGmtModified());
        notification.setReceiver(receiver);
        notificationMapper.insert(notification);
    }

    public ResultDTO selectCommentList(Integer id,CommentTypeEnum commentTypeEnum) {
        CommentExample commentExample=new CommentExample();
        if (commentTypeEnum==CommentTypeEnum.Comment){
            commentExample.createCriteria().andTargetIdEqualTo(id);
        }else if (commentTypeEnum==CommentTypeEnum.Question){
            commentExample.createCriteria().andParentIdEqualTo(id).andTargetIdEqualTo(0);
        }
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size()==0) {
            return ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
        }
        Set<String> collects = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<String> userIds=new ArrayList<>();
        userIds.addAll(collects);
        List<Integer> users=new ArrayList<>();
        for (String userId : userIds) {
            users.add(Integer.parseInt(userId));
        }
        UserExample userExample=new UserExample();
        userExample.createCriteria().andIdIn(users);
        List<User> userList=userMapper.selectByExample(userExample);
        Map<Integer, User> userMap=userList.stream().collect(Collectors.toMap(user->user.getId()
                ,user->user));
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(Integer.parseInt(comment.getCommentator())));
            return commentDTO;
        }).collect(Collectors.toList());
        return ResultDTO.success(commentDTOS);
    }

}
