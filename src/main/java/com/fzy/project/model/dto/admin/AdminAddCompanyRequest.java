package com.fzy.project.model.dto.admin;

import lombok.Data;

@Data
public class AdminAddCompanyRequest {

    private String companyPhone;
    private String companyPassword;
    private String companyName;
    private String checkPassword;

}
