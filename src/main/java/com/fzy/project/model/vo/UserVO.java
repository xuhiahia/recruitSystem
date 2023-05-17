package com.fzy.project.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图
 *
 * @TableName user
 */
@Data
public class UserVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 角色
     */
    private String role;
    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private String gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 邮箱
     */
    private String userEmail;

    private static final long serialVersionUID = 1L;
}