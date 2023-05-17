package com.fzy.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName chat_list
 */
@TableName(value ="chat_list")
@Data
public class ChatList implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "user_chat_id")
    private Long userChatId;

    /**
     * 
     */
    @TableField(value = "send_user")
    private Long sendUser;

    /**
     * 
     */
    @TableField(value = "receive_user")
    private Long receiveUser;

    /**
     * 发送方是否还在窗口
     */
    @TableField(value = "send_window")
    private Integer sendWindow;

    /**
     * 接收方是否还在窗口 0-在 1-不在
     */
    @TableField(value = "receive_window")
    private Integer receiveWindow;

    /**
     * 
     */
    @TableField(value = "unread")
    private Integer unread;

    /**
     * 列表状态， 0不删除 1删除
     */
    @TableField(value = "status")
    private Integer status;

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