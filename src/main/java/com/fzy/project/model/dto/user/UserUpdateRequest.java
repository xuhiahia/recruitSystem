package com.fzy.project.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求
 *
 * @author ShuaiGeF
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;


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
     * 年龄
     */
    private Integer age;

    /**
     * 电子邮箱
     */
    private String userEmail;

    private String pwd;

    private String newPwd;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}