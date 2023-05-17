package com.fzy.project.model.dto.chief;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class ChiefDeleteRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private List<Long> ids;
}
