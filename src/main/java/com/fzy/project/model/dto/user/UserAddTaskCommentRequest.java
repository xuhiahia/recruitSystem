package com.fzy.project.model.dto.user;

import lombok.Data;

@Data
public class UserAddTaskCommentRequest {

    private String userComment;

    private Long userId;

    private Long taskId;

    private Long companyId;


}
