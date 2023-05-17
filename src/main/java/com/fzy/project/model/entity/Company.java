package com.fzy.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName company
 */
@TableName(value ="company")
@Data
public class Company implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 
     */
    @TableField(value = "role")
    private Integer role;

    /**
     * 
     */
    @TableField(value = "company_account")
    private String companyAccount;

    /**
     * 
     */
    @TableField(value = "company_pwd")
    private String companyPwd;

    /**
     * 
     */
    @TableField(value = "company_name")
    private String companyName;

    /**
     * 
     */
    @TableField(value = "company_industry")
    private String companyIndustry;

    /**
     * 0-小，1-中。2-大
     */
    @TableField(value = "scale")
    private Integer scale;

    /**
     * 
     */
    @TableField(value = "address")
    private String address;

    /**
     * 
     */
    @TableField(value = "company_email")
    private String companyEmail;

    /**
     * 
     */
    @TableField(value = "company_phone")
    private String companyPhone;

    /**
     * 企业状态0-启用 1-停用
     */
    @TableField(value = "company_status")
    private Integer companyStatus;

    /**
     * 企业备注
     */
    @TableField(value = "mark")
    private String mark;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "company_avatar")
    private String companyAvatar;
    /**
     * 
     */
    @TableLogic
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}