package com.fzy.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户-岗位对应表
 * @TableName user_chief
 */
@TableName(value ="user_chief")
@Data
public class UserChief implements Serializable {
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
    @TableField(value = "chief_id")
    private Long chiefId;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 应聘状态0-待定 1-通过
     */
    @TableField(value = "user_chief_status")
    private Integer userChiefStatus;

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