package com.fzy.project.model.dto.user;


import lombok.Data;

@Data
public class UserCommitTaskRequest {

    private Long userId;

    private Long companyId;

    private Long taskId;

    private String userCommit;

    private String userComment;

}
