package com.fzy.project.model.vo;

import lombok.Data;

@Data
public class BlogCommentVO {

    private Long commentId;

    private Long comments;

    private String userName;

    private String userAvatar;

    private String commentContent;

    private String createTime;

}
