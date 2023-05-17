package com.fzy.project.model.dto.company;

import lombok.Data;

import java.util.List;

@Data
public class CompanyTaskDeleteRequest {

    private List<Long> ids;

    private Long companyId;

}
