package com.fzy.project.model.dto.company;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * 用于管理员对企业的修改
 */
@Data
public class CompanyUpdateRequest {

    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;


    /**
     *
     */
    private String companyName;

    /**
     *
     */

    private String companyIndustry;

    /**
     * 0-小，1-中。2-大
     */
    private String scale;

    /**
     *
     */

    private String address;

    /**
     *
     */
    private String companyEmail;

    /**
     *
     */
    private String companyPhone;


    /**
     * 企业备注
     */
    private String mark;

    /**
     * 企业状态0-启用 1-停用
     */
    private String companyStatus;
}
