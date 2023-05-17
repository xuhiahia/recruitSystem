package com.fzy.project.model.dto.blog;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class BlogAddRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String blogContent;

    private String blogTitle;

    private List<String> blogImages;

    private String blogType;

}
