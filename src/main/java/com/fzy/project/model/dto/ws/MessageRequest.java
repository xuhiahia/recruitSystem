package com.fzy.project.model.dto.ws;

import lombok.Data;

@Data
public class MessageRequest {

    private Long sendUser;

    private Long receiveUser;

    private String chatComment;

//    private Long userChatId;
}
