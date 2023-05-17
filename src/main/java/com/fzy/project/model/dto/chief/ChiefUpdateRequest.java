package com.fzy.project.model.dto.chief;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class ChiefUpdateRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String chiefSalary;

    private String chiefAddress;

    private String chiefDescription;

    private String chiefCommand;

    private String chiefName;

    private String chiefTime;

    private String chiefHc;

    private String chiefRealHc;
}
