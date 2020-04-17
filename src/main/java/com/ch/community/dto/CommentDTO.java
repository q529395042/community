package com.ch.community.dto;

import com.ch.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private String commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer likeCount;
    private String content;
    private Integer commentCount;
    private Integer targetId;

    private User user;
}
