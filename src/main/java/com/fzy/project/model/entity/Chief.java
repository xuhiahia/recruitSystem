package com.fzy.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 招聘岗位表
 * @TableName chief
 */
@TableName(value ="chief")
@Data
public class Chief implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 
     */
    @TableField(value = "chief_salary")
    private String chiefSalary;

    /**
     * 
     */
    @TableField(value = "chief_address")
    private String chiefAddress;

    /**
     * 
     */
    @TableField(value = "chief_description")
    private String chiefDescription;

    /**
     * 
     */
    @TableField(value = "chief_command")
    private String chiefCommand;

    /**
     * 
     */
    @TableField(value = "chief_name")
    private String chiefName;

    /**
     * 
     */
    @TableField(value = "chief_time")
    private String chiefTime;

    /**
     * 招聘人数
     */
    @TableField(value = "chief_hc")
    private Integer chiefHc;

    /**
     * 
     */
    @TableField(value = "chief_real_hc")
    private Integer chiefRealHc;

    /**
     * 0-审核 1-通过
     */
    @TableField(value = "chief_status")
    private Integer chiefStatus;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}