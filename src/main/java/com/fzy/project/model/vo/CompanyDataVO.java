package com.fzy.project.model.vo;

import lombok.Data;

@Data
public class CompanyDataVO {

    private Long id;

    private String companyAccount;

    private String companyName;

    private String companyIndustry;

    /**
     * 0-小，1-中。2-大
     */

    private String scale;

    private String address;

    private String companyEmail;

    private String companyPhone;

    /**
     * 企业备注
     */
    private String mark;

    private String companyAvatar;
}
