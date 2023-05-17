package com.fzy.project.model.dto.common;

import lombok.Data;

@Data
public class AddRelationRequest {

    private Long sendId;

    private Long receiveId;
}
