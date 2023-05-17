package com.fzy.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 企业项目用户对应表
 * @TableName user_task
 */
@TableName(value ="user_task")
@Data
public class UserTask implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 
     */
    @TableField(value = "task_id")
    private Long taskId;

    /**
     * 用户传的附件
     */
    @TableField(value = "user_submit")
    private String userSubmit;

    /**
     * 用户评论
     */
    @TableField(value = "user_comment")
    private String userComment;

    /**
     * 
     */
    @TableField(value = "task_type")
    private String taskType;

    /**
     * 任务标题
     */
    @TableField(value = "task_title")
    private String taskTitle;

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