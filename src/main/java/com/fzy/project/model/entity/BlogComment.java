package com.fzy.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文章评论
 * @TableName blog_comment
 */
@TableName(value ="blog_comment")
@Data
public class BlogComment implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "blog_id")
    private Long blogId;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 默认0，一级评论
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 
     */
    @TableField(value = "blog_comment_content")
    private String blogCommentContent;

    /**
     * 回答的用户id
     */
    @TableField(value = "answer_id")
    private Long answerId;

    /**
     * 默认0 1-被举报 2-禁用
     */
    @TableField(value = "blog_comment_status")
    private Integer blogCommentStatus;

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