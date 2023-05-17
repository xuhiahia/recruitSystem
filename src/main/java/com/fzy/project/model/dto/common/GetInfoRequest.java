package com.fzy.project.model.dto.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class GetInfoRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String role;
}
