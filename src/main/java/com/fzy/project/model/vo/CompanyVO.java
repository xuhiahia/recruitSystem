package com.fzy.project.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class CompanyVO {
    /**
     *
     */
    private Long id;

    private String role;

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
    /**
     * 企业状态0-启用 1-停用
     */

    private String companyStatus;

    private String companyAvatar;

    private String createTime;


}
