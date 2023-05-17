package com.fzy.project.model.dto.user;

import lombok.Data;

@Data
public class UserCommitNoteRequest {

    private Long userId;

    private Long companyId;

    private Long chiefId;

    private String noteUrl;


}
