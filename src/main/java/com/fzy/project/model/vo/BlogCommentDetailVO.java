package com.fzy.project.model.vo;

import lombok.Data;

@Data
public class BlogCommentDetailVO {

    private Long commentId;

    private String userName;

    private String userAvatar;

    private String answerName;

    private String answerAvatar;

    private String blogCommentContent;

    private String createTime;

}
