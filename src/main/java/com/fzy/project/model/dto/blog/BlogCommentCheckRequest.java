package com.fzy.project.model.dto.blog;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class BlogCommentCheckRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long commentId;
    //通过 恢复正常 禁用-举报成功
    private String choice;
}
