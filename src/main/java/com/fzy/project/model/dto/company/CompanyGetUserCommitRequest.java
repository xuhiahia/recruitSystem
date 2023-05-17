package com.fzy.project.model.dto.company;

import lombok.Data;

@Data
public class CompanyGetUserCommitRequest {

    private String companyId;

    private String taskType;

    private String taskTitle;

}
