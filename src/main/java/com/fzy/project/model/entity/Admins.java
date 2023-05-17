package com.fzy.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName admins
 */
@TableName(value ="admins")
@Data
public class Admins implements Serializable {
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
    @TableField(value = "admin_account")
    private String adminAccount;

    /**
     * 
     */
    @TableField(value = "admin_pwd")
    private String adminPwd;

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

    @TableField(value = "admin_avatar")
    private String adminAvatar;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}