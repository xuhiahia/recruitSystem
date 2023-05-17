package com.fzy.project.model.dto.ws;

import lombok.Data;

@Data
public class MessageAddRelationRequest {

    private Long sendUser;

    private Long receiveUser;
}
