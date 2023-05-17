package com.fzy.project.model.dto.company;

import lombok.Data;

@Data
public class CompanyRegisterRequest {

    private String companyAccount;

    private String companyCheckPwd;

    private String companyPwd;

    private String companyName;

    private String companyIndustry;

    private String companyAddress;

    private String mark;

    private String companyPhone;

    private String companyEmail;

    private String companyScale;
}
