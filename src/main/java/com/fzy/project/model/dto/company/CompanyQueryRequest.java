package com.fzy.project.model.dto.company;

import com.fzy.project.common.PageRequest;
import lombok.Data;

@Data
public class CompanyQueryRequest extends PageRequest {

    private String companyName;

    private String companyAccount;

    private String scale;

    private String companyIndustry;

    private String address;

    private String companyStatus;

}
