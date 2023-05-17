package com.fzy.project.model.dto.chief;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class ChiefQueryRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long companyId;

    private String chiefAddress;

    private String chiefName;

    private String chiefStatus;
}
