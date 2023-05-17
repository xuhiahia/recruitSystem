package com.fzy.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 聊天内容
 * @TableName chat_message
 */
@TableName(value ="chat_message")
@Data
public class ChatMessage implements Serializable {
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
     * 发送用户的id
     */
    @TableField(value = "send_user")
    private Long sendUser;

    /**
     * 
     */
    @TableField(value = "receive_user")
    private Long receiveUser;

    /**
     * 
     */
    @TableField(value = "chat_content")
    private String chatContent;

    /**
     * 是否是最后一条消息 0是 1不是
     */
    @TableField(value = "is_latest")
    private Integer isLatest;

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