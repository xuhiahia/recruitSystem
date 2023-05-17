package com.fzy.project.model.dto.company;

import lombok.Data;

@Data
public class CompanyAddTaskRequest {

    private Long companyId;

    private String taskTitle;

    private String taskContent;

    private String taskCommand;

    private String taskType;

}
