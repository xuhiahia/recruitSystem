package com.fzy.project.model.dto.ws;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendMessageRequest implements Serializable {

    private String remoteUser;


    private static final long serialVersionUID = 1L;

}
