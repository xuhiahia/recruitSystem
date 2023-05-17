package com.fzy.project.model.dto.blog;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class BlogLikeRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long blogId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
}
