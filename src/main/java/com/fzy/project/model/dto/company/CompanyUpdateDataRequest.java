package com.fzy.project.model.dto.company;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 用于企业自身修改信息
 */
@Data
public class CompanyUpdateDataRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String companyName;

    private String companyAvatar;

    private String companyIndustry;

    private String companyAddress;

    private String mark;

    private String companyPhone;

    private String companyEmail;

    private String companyScale;

    private String oldPwd;

    private String newPwd;
}
