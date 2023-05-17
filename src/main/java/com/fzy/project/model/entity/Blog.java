package com.fzy.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文章
 * @TableName blog
 */
@TableName(value ="blog")
@Data
public class Blog implements Serializable {
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
    @TableField(value = "blog_content")
    private String blogContent;

    /**
     * 
     */
    @TableField(value = "blog_title")
    private String blogTitle;

    /**
     * 
     */
    @TableField(value = "blog_images")
    private String blogImages;

    /**
     * 
     */
    @TableField(value = "blog_comments")
    private Integer blogComments;

    /**
     * 
     */
    @TableField(value = "blog_likes")
    private Integer blogLikes;

    /**
     * 
     */
    @TableField(value = "blog_collections")
    private Integer blogCollections;

    /**
     *默认0-审核，1-正常，2被举报，3禁用
     */
    @TableField(value = "blog_status")
    private Integer blogStatus;

    @TableField(value = "blog_type")
    private String blogType;

    @TableField(value = "blog_shares")
    private Integer blogShares;
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