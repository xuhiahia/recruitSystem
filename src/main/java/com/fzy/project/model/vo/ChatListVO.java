package com.fzy.project.model.vo;

import lombok.Data;

@Data
public class ChatListVO {

    private Long userChatId;

    private Long receiveId;

    private String receiveName;

    private String receiveAvatar;

    private String lastMessage;

    private String createTime;

    private Integer unread;
}
