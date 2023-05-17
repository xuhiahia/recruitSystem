package com.fzy.project.model.dto.chief;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class ChiefAddRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long companyId;

    private String chiefName;

    private String chiefSalary;

    private String chiefAddress;

    private String chiefDescription;

    private String chiefCommand;

    private String chiefTime;

    private String chiefHc;

}
