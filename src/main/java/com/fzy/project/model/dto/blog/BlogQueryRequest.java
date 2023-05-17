package com.fzy.project.model.dto.blog;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class BlogQueryRequest {

    private String userId;

    private String blogType;

    private String blogTitle;


}
