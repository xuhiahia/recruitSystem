package com.fzy.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 简历内容
 * @TableName note
 */
@TableName(value ="note")
@Data
public class Note implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 个人描述
     */
    @TableField(value = "note_description")
    private String noteDescription;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 
     */
    @TableField(value = "academic_degree")
    private String academicDegree;

    /**
     * 0-离校随时到岗  1-在校月内到岗  2-在校考虑机会 3-在校暂不考虑
     */
    @TableField(value = "job_status")
    private Integer jobStatus;

    /**
     * 
     */
    @TableField(value = "graduate_year")
    private String graduateYear;

    /**
     * 
     */
    @TableField(value = "graduate_school")
    private String graduateSchool;

    /**
     * 教育经历
     */
    @TableField(value = "note_education")
    private String noteEducation;

    /**
     * 项目经历
     */
    @TableField(value = "note_item")
    private String noteItem;

    /**
     * 求职期望
     */
    @TableField(value = "note_expect")
    private String noteExpect;

    /**
     * 工作经历
     */
    @TableField(value = "note_work")
    private String noteWork;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "note_url")
    private String noteUrl;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}