package com.fzy.project.model.dto.note;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class NotePassRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long noteId;
}
