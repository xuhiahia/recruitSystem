package com.fzy.project.model.dto.company;

import lombok.Data;

@Data
public class CompanyTaskQueryRequest {

    private String companyId;

    private String taskTitle;

    private String taskType;
}
