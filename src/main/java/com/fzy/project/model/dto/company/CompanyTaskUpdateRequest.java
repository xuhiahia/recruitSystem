package com.fzy.project.model.dto.company;


import lombok.Data;

@Data
public class CompanyTaskUpdateRequest {

    private Long id;//taskId

    private String taskTitle;

    private String taskContent;

    private String taskCommand;

    private String taskType;

    //发布-0 停用1
    private String taskStatus;

}
