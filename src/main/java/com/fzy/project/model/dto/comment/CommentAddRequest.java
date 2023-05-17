package com.fzy.project.model.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class CommentAddRequest {
//    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    //回答的评论id
//    @JsonSerialize(using = ToStringSerializer.class)
    private Long answerId;

//    @JsonSerialize(using = ToStringSerializer.class)
    private Long blogId;

    private String blogCommentContent;

    //评论主的id
//    @JsonSerialize(using = ToStringSerializer.class)
    private Long commentId;



    /**
     * 只有二级评论和三级评论有 commentId和answerId
     */

}
