package com.fzy.project.model.dto.note;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class NoteQueryRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long companyId;

    private String chiefName;
}
