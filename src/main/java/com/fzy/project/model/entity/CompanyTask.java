package com.fzy.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 企业项目
 * @TableName company_task
 */
@TableName(value ="company_task")
@Data
public class CompanyTask implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 企业Id
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 项目标题
     */
    @TableField(value = "task_title")
    private String taskTitle;

    /**
     * 
     */
    @TableField(value = "task_content")
    private String taskContent;

    /**
     * 
     */
    @TableField(value = "task_command")
    private String taskCommand;

    /**
     * 默认0-发布 1-停用
     */
    @TableField(value = "task_status")
    private Integer taskStatus;

    /**
     * 项目类型
     */
    @TableField(value = "task_type")
    private String taskType;

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