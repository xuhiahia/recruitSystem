package com.fzy.project.model.vo;

import lombok.Data;

@Data
public class CompanyTaskVO {

    private Long id;

    private String companyName;

    private String companyAvatar;

    private Long companyId;

    private String taskTitle;

    private String taskContent;

    private String taskCommand;

    private String taskType;

    //发布 停用
    private String taskStatus;

}
