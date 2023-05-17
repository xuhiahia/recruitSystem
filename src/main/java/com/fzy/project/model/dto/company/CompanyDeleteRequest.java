package com.fzy.project.model.dto.company;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDeleteRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private List<Long> ids;


}
