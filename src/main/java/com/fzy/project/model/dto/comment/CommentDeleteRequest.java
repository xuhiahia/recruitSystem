package com.fzy.project.model.dto.comment;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CommentDeleteRequest {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long commentId;

    private Long blogId;
}
