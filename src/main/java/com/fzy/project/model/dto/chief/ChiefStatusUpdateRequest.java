package com.fzy.project.model.dto.chief;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class ChiefStatusUpdateRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
}
