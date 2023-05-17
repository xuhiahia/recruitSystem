package com.fzy.project.model.vo;

import lombok.Data;

@Data
public class AdminCommentVO {

    private String blogTitle;

    private String commentContent;

    private Long commentId;

    private String createTime;

    private String userName;

    private String userAvatar;

}
